package com.ibm.sterling.bfg.app.service.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.exception.certificate.CertificateIntegrationException;
import com.ibm.sterling.bfg.app.model.audit.ActionType;
import com.ibm.sterling.bfg.app.model.audit.AdminAuditEventRequest;
import com.ibm.sterling.bfg.app.model.audit.EventType;
import com.ibm.sterling.bfg.app.model.audit.Type;
import com.ibm.sterling.bfg.app.model.certificate.*;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import com.ibm.sterling.bfg.app.repository.certificate.ChangeControlCertRepository;
import com.ibm.sterling.bfg.app.repository.certificate.TrustedCertificateRepository;
import com.ibm.sterling.bfg.app.service.audit.AdminAuditService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.naming.InvalidNameException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus.ACCEPTED;
import static com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus.PENDING;
import static com.ibm.sterling.bfg.app.service.certificate.ImportCertificatesConstants.*;

@Service
@Transactional(timeout = 100000)
public class ImportedTrustedCertificateService {
    private static final Logger LOG = LogManager.getLogger(ImportedTrustedCertificateService.class);

    @Autowired
    private ChangeControlCertService changeControlCertService;

    @Autowired
    private ChangeControlCertRepository changeControlCertRepository;

    @Autowired
    private TrustedCertificateDetailsService trustedCertificateDetailsService;

    @Autowired
    private CertificateIntegrationService certificateIntegrationService;

    @Autowired
    private TrustedCertificateService trustedCertificateService;

    @Autowired
    private TrustedCertificateRepository trustedCertificateRepository;

    @Autowired
    private AdminAuditService adminAuditService;

    @PostConstruct
    public void importCertificatesFromB2B() throws CertificateIntegrationException, JsonProcessingException {
        LOG.info("Import the certs from B2B");
        try {
            List<ImportedTrustedCertificateDetails> importedTrustedCertificateDetails =
                    new ArrayList<>(certificateIntegrationService.getCertificates()
                            .stream()
                            .collect(Collectors.toMap(
                                    IntegratedCertificateData::getCertNameAndDate,
                                    integratedCertificateData -> {
                                        try {
                                            return integratedCertificateData.convertToX509Certificate();
                                        } catch (CertificateException e) {
                                            return null;
                                        }
                                    }))
                            .entrySet()
                            .stream()
                            .collect(Collectors.toMap(
                                    entry -> entry.getKey().getCertName(),
                                    entry -> {
                                        try {
                                            ImportedTrustedCertificateDetails trustedCertificateDetails =
                                                    new ImportedTrustedCertificateDetails(trustedCertificateDetailsService
                                                            .getTrustedCertificateDetails(entry.getValue(), true));
                                            trustedCertificateDetails.setCertNameAndDate(entry.getKey());
                                            trustedCertificateDetails.setCertificate(entry.getValue());
                                            trustedCertificateDetailsService.checkCertNameUniquenessLocally(
                                                    trustedCertificateDetails, entry.getKey().getCertName());
                                            return trustedCertificateDetails;
                                        } catch (NoSuchAlgorithmException | CertificateEncodingException | InvalidNameException | JsonProcessingException e) {
                                            return null;
                                        }
                                    }
                            ))
                            .values());


            importedTrustedCertificateDetails
                    .stream()
                    .collect(Collectors.groupingBy(
                            TrustedCertificateDetails::getThumbprint,
                            Collectors.toList()
                    ))
                    .values()
                    .forEach(value ->
                            checkValidation(value, "thumbprint", "SHA-1 Thumbprint is not unique"));

            importedTrustedCertificateDetails
                    .stream()
                    .collect(Collectors.groupingBy(
                            TrustedCertificateDetails::getThumbprint256,
                            Collectors.toList()
                    ))
                    .values()
                    .forEach(value ->
                            checkValidation(value, "thumbprint256", "SHA-2 Thumbprint is not unique"));

            Map<Boolean, List<ImportedTrustedCertificateDetails>> booleanMap = importedTrustedCertificateDetails
                    .stream()
                    .collect(Collectors.partitioningBy(
                            TrustedCertificateDetails::isValid,
                            Collectors.toList()
                    ));

            List<TrustedCertificate> deletedCertsInSBI = trustedCertificateService
                    .listAll()
                    .stream()
                    .filter(isNotExistsInSBIList(booleanMap.get(false)))
                    .collect(Collectors.toList());

            deletedCertsInSBI
                    .forEach(tc ->
                            proceedImportedCertificate(
                                    tc, Operation.DELETE, String.format(DELETE_COMMENT, tc.getCertificateName())));

            booleanMap.get(true)
                    .stream()
                    .map(ImportedTrustedCertificateDetails::convertToTrustedCertificate)
                    .forEach(tc -> proceedImportedCertificate(tc, Operation.CREATE, IMPORT_COMMENT));

            booleanMap.get(false)
                    .forEach(value -> {
                                LOG.info("Ignore imported trusted certificate {}", value.getCertNameAndDate().getCertName());
                                adminAuditService.fireAdminAuditEvent(
                                        new AdminAuditEventRequest(
                                                ACTION_BY,
                                                ActionType.valueOf(Operation.CREATE.name()),
                                                EventType.valueOf(ChangeControlStatus.REJECTED.name()),
                                                Type.TRUSTED_CERTIFICATE,
                                                "n/a",
                                                actionValueIgnored.apply(value.getCertNameAndDate().getCertName(),
                                                        value.getCertificateErrors())
                                        ));
                            }
                    );
        } catch (CertificateIntegrationException ex) {
            LOG.error("Caught CertificateIntegrationException - {}", ex.getErrorMessage());
        }
    }

    private Predicate<TrustedCertificate> isNotExistsInSBIList(List<ImportedTrustedCertificateDetails> listFromSBI) {
        return cert ->
                listFromSBI.stream()
                        .noneMatch(certDetail ->
                                certDetail.getCertNameAndDate().getCertName().equals(cert.getCertificateName())
                        )
                ;

    }

    private void checkValidation(List<ImportedTrustedCertificateDetails> importedTrustedCertificateDetails,
                                 String errorKey, String errorValue) {
        Collections.sort(importedTrustedCertificateDetails);
        importedTrustedCertificateDetails.get(0).setLatest(true);
        importedTrustedCertificateDetails
                .stream()
                .filter(cert -> !cert.isLatest())
                .forEach(detail ->
                        trustedCertificateDetailsService.addError(detail, errorKey, errorValue));
    }

    private void proceedImportedCertificate(TrustedCertificate trustedCertificate, Operation operation, String comment) {
        try {
            LOG.info("Trying to {} trusted certificate {} ", operation, trustedCertificate);
            LOG.info("Creating the change control");
            ChangeControlCert changeControlCert = new ChangeControlCert();
            changeControlCert.setOperation(operation);
            changeControlCert.setStatus(ACCEPTED);
            changeControlCert.setChanger(ACTION_BY);
            changeControlCert.setApprover(ACTION_BY);
            changeControlCert.setApproverComments(comment);
            changeControlCert.setResultMeta1(trustedCertificate.getCertificateName());
            changeControlCert.setResultMeta2(trustedCertificate.getThumbprint());
            changeControlCert.setResultMeta3(trustedCertificate.getThumbprint256());
            changeControlCert.setTrustedCertificateLog(new TrustedCertificateLog(trustedCertificate));
            changeControlCert.getTrustedCertificateLog().setCertificate(null);
            if (operation.equals(Operation.CREATE)) {
                changeControlCert.getTrustedCertificateLog().setCertificateId(
                        trustedCertificateService.save(trustedCertificate).getCertificateId());
                LOG.info("Persist trusted certificate {}", trustedCertificate);
                changeControlCertService.save(changeControlCert);
                LOG.info("Persisted CC {}", changeControlCert);
            } else {
                changeControlCert.getTrustedCertificateLog().setCertificateId(
                        trustedCertificate.getCertificateId());
                LOG.info("Deleted trusted certificate {}", trustedCertificate);
                changeControlCertService.save(changeControlCert);
                LOG.info("Checking pending change for trusted certificate {}", trustedCertificate);
                if (changeControlCertRepository.existsByResultMeta1AndStatus(
                        trustedCertificate.getCertificateName(), PENDING)) {

                    ChangeControlCert pendingChangeControl = changeControlCertRepository
                            .findByResultMeta1AndStatus(trustedCertificate.getCertificateName(), PENDING);
                    LOG.info("Found pending change {}, proceed deletion", pendingChangeControl.getChangeID());
                    pendingChangeControl.setChanger(ACTION_BY);
                    trustedCertificateService.cancelPendingCertificate(pendingChangeControl);
                }
                trustedCertificateRepository.delete(trustedCertificate);
            }
            adminAuditService.fireAdminAuditEvent(new AdminAuditEventRequest(changeControlCert, changeControlCert.getApprover()));
        } catch (RuntimeException e) {
            LOG.info("Fail with importing {}", trustedCertificate);
            adminAuditService.fireAdminAuditEvent(
                    new AdminAuditEventRequest(
                            ACTION_BY,
                            ActionType.valueOf(Operation.CREATE.name()),
                            EventType.valueOf(ChangeControlStatus.REJECTED.name()),
                            Type.TRUSTED_CERTIFICATE,
                            "n/a",
                            actionValueFailed.apply(trustedCertificate.getCertificateName(),
                                    e.getLocalizedMessage())));
        }
    }

    private BiFunction<String, List<Map<String, List<String>>>, String> actionValueIgnored = (certName, certificateErrors) ->
            new Formatter().format(ACTION_VALUE_IGNORED,
                    certName, convertCertificateErrorsListToString(certificateErrors)).toString();

    private BiFunction<String, String, String> actionValueFailed = (certName, failureMessage) ->
            new Formatter().format(ACTION_VALUE_FAILED, certName, failureMessage).toString();

    private String convertCertificateErrorsListToString(List<Map<String, List<String>>> certificateErrors) {
        return certificateErrors.stream()
                .flatMap(error -> {
                    List<String> errors = new ArrayList<>();
                    error.values().forEach(errors::addAll);
                    return errors.stream();
                })
                .collect(Collectors.joining(", "));
    }
}
