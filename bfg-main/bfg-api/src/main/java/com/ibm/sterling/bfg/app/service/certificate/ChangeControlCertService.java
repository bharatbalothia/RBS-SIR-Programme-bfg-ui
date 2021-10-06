package com.ibm.sterling.bfg.app.service.certificate;

import com.ibm.sterling.bfg.app.model.certificate.ChangeControlCert;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificate;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChangeControlCertService {
    List<ChangeControlCert> listAll();

    ChangeControlCert getChangeControlCertById(String id);

    @Transactional
    ChangeControlCert save(ChangeControlCert changeControl);

    @Transactional
    ChangeControlCert updateStatus(String changeControlId, ChangeControlStatus status);

    @Transactional
    void setApproveInfo(ChangeControlCert changeControl, String user,
                        String comments, ChangeControlStatus status);

    List<ChangeControlCert> findPendingChangeControlsAsc(String certName, String thumbprint);

    List<ChangeControlCert> findPendingChangeControls(String certName, String thumbprint);

    @Transactional
    TrustedCertificate updateChangeControlCert(ChangeControlCert changeControlCert, String certName, String changerComments);

    @Transactional
    void deleteChangeControl(ChangeControlCert changeControlCert);

    void checkOnPendingState(String certName);
}
