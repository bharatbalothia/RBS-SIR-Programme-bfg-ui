package com.ibm.sterling.bfg.app.service.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.exception.CertificateNotFoundException;
import com.ibm.sterling.bfg.app.model.CertType;
import com.ibm.sterling.bfg.app.model.certificate.ChangeControlCert;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificate;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificateDetails;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificateLog;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changeControl.Operation;
import com.ibm.sterling.bfg.app.repository.certificate.TrustedCertificateRepository;
import com.ibm.sterling.bfg.app.service.CertificateValidationService;
import com.ibm.sterling.bfg.app.service.GenericSpecification;
import com.ibm.sterling.bfg.app.utils.ListToPageConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.InvalidNameException;
import javax.security.cert.CertificateEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import static com.ibm.sterling.bfg.app.model.changeControl.Operation.CREATE;

@Service
public class TrustedCertificateImplService implements TrustedCertificateService {
    private static final Logger LOG = LogManager.getLogger(TrustedCertificateImplService.class);

    @Autowired
    private TrustedCertificateRepository certificateRepository;

    @Autowired
    private ChangeControlCertService changeControlService;

    @Autowired
    private CertificateValidationService certificateValidationService;

    @Override
    public List<TrustedCertificate> listAll() {
        return certificateRepository.findAll();
    }

    @Override
    public TrustedCertificate findById(String id) throws JsonProcessingException {
        LOG.info("Trusted certificate by id {}", id);
        TrustedCertificate certById = certificateRepository.findById(id)
                .orElseThrow(CertificateNotFoundException::new);
        setAuthChainReport(certById);
        return certById;
    }

    private void setAuthChainReport(TrustedCertificate certById) throws JsonProcessingException {
        certById.setAuthChainReport(findAuthChain(certById.getIssuer()));
    }

    public TrustedCertificate convertX509CertificateToTrustedCertificate(X509Certificate x509Certificate,
                                                                         String certName,
                                                                         String comment)
            throws CertificateException, InvalidNameException, NoSuchAlgorithmException, JsonProcessingException, CertificateEncodingException {

        TrustedCertificateDetails trustedCertificateDetails =
                new TrustedCertificateDetails(x509Certificate, certificateValidationService);
        TrustedCertificate trustedCertificate = trustedCertificateDetails.convertToTrustedCertificate();
        trustedCertificate.setCertificateName(certName);
        trustedCertificate.setChangerComments(comment);
//        trustedCertificate.setCertificate(x509Certificate);
        return saveCertificateToChangeControl(trustedCertificate, CREATE);
    }

    @Override
    public TrustedCertificate saveCertificateToChangeControl(TrustedCertificate cert, Operation operation) throws CertificateException {
        LOG.info("Trying to save trusted certificate {} to change control", cert);
        ChangeControlCert changeControl = new ChangeControlCert();
        changeControl.setOperation(operation);
        changeControl.setChanger(SecurityContextHolder.getContext().getAuthentication().getName());
        changeControl.setChangerComments(cert.getChangerComments());
        changeControl.setResultMeta1(cert.getCertificateName());
        changeControl.setResultMeta2(cert.getCertificateThumbprint());
        changeControl.setTrustedCertificateLog(new TrustedCertificateLog(cert));
        cert.setChangeID(changeControlService.save(changeControl).getChangeID());
        return cert;
    }

    @Override
    public TrustedCertificate getTrustedCertificateAfterApprove(ChangeControlCert changeControl, String approverComments, ChangeControlStatus status) throws Exception {
        if (changeControl.getStatus() != ChangeControlStatus.PENDING) {
            throw new Exception("Status is not pending and therefore no action can be taken");
        }
        TrustedCertificate cert = new TrustedCertificate();
        switch (status) {
            case ACCEPTED:
                cert = approve(changeControl);
                break;
            case FAILED:
            case REJECTED:
        }
        changeControlService.setApproveInfo(
                changeControl,
                SecurityContextHolder.getContext().getAuthentication().getName(),
                approverComments,
                status);
        setAuthChainReport(cert);
        return cert;
    }

    private TrustedCertificate approve(ChangeControlCert changeControl)
            throws JsonProcessingException, CertificateException {
        LOG.info("Trusted certificate {} action", changeControl.getOperation());
        TrustedCertificate cert = saveTrustedCertificateAfterApprove(changeControl);
        LOG.info("Trusted certificate after {} action: {}", changeControl.getOperation(), cert);
        return cert;
    }

    private TrustedCertificate saveTrustedCertificateAfterApprove(ChangeControlCert changeControl)
            throws JsonProcessingException, CertificateException {
        LOG.info("Approve the Trusted certificate {} action", changeControl.getOperation());
        TrustedCertificate cert = changeControl.convertTrustedCertificateLogToTrustedCertificate();

        Operation operation = changeControl.getOperation();
        if (operation.equals(Operation.DELETE)) {
            certificateRepository.delete(cert);
        } else {
            certificateRepository.save(cert);
        }
        setAuthChainReport(cert);
        LOG.info("Saved trusted certificate to DB {}", cert);
        TrustedCertificateLog certLog = changeControl.getTrustedCertificateLog();
        certLog.setCertificateId(cert.getCertificateId());
        changeControl.setTrustedCertificateLog(certLog);
        changeControlService.save(changeControl);
        return cert;
    }

    @Override
    public Page<CertType> findEntities(Pageable pageable, String certName, String thumbprint) {
        LOG.info("Search trusted certificates by trusted certificate name {} and thumbprint {}", certName, thumbprint);
        List<CertType> certificates = new ArrayList<>(changeControlService.findAllPending(certName, thumbprint));
        Specification<TrustedCertificate> specification = Specification
                .where(
                        GenericSpecification.<TrustedCertificate>filter(certName, "certificateName"))
                .and(
                        GenericSpecification.filter(thumbprint, "certificateThumbprint"));
        certificates.addAll(
                certificateRepository
                        .findAll(specification));
        certificates.sort(Comparator.comparing(o -> o.nameForSorting().toLowerCase()));
        return ListToPageConverter.convertListToPage(certificates, pageable);
    }

    private List<Map<String, String>> findAuthChain(Map<String, List<String>> issuer) throws JsonProcessingException {
        List<String> rdnKeys = new ArrayList<>(issuer.keySet());
        Collections.reverse(rdnKeys);
        return certificateValidationService.getCertificateChain(
                Base64.getEncoder().encodeToString(
                        rdnKeys.stream()
                                .map(constValue -> constValue + "=" +
                                        issuer.get(constValue).stream()
                                                .map(issuerValueByKey -> issuerValueByKey.replace(",", "\\"))
                                                .reduce("", (issuerValueByKeyOne, issuerValueByKeyTwo) ->
                                                        issuerValueByKeyOne + issuerValueByKeyTwo
                                                ))
                                .reduce("", (issuerValueOne, issuerValueTwo) -> issuerValueOne + issuerValueTwo)
                                .getBytes())
        );
    }
}
