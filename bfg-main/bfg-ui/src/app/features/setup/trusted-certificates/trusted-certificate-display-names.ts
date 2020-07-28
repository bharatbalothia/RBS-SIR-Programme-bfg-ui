import { TrustedCertificate } from 'src/app/shared/models/trustedCertificate/trusted-certificate.model';
import { Tab } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { DIALOG_TABS } from 'src/app/core/constants/dialog-tabs';
import { difference } from 'src/app/shared/utils/utils';
import { ChangeControl } from 'src/app/shared/models/changeControl/change-control.model';
import { CHANGE_OPERATION } from 'src/app/shared/models/changeControl/change-operation';

export const TRUSTED_CERTIFICATE_DISPLAY_NAMES = {
    serialNumber: 'Serial Number',
    thumbprint: 'SHA-1 Thumbprint',
    thumbprint256: 'SHA-2 Thumbprint',
    validDates: 'Valid Dates',
    startDate: 'Start Date',
    endDate: 'End Date',
    issuer: 'Issuer',
    subject: 'Subject',
    CN: 'Common Name',
    OU: 'Organization Unit',
    O: 'Organization',
    L: 'Locality',
    C: 'Country',
    ST: 'State or Province',
    changerComments: 'Changer Comments',
    authChainReport: 'Auth Chain Report',
    subjectDN: 'Subject DN',
    certificateName: 'Certificate Name',
    changeID: 'Change ID',
    objectType: 'Object type',
    status: 'Status',
    approver: 'Approver',
    approverComments: 'Approver Notes',
    changer: 'Changer',
    operation: 'Operation',
    dateChanged: 'Date Changed',
};

export const getTrustedCertificateDisplayName = (key: string) => TRUSTED_CERTIFICATE_DISPLAY_NAMES[key] || key;

export const getTrustedCertificateItemInfoValues = (item) => item && Object.keys(item).map(key => `${getTrustedCertificateDisplayName(key)}: ${item[key]}`).sort();

const getTrustedCertificateDetailsSectionItems = (trustedCertificate: TrustedCertificate) => ({
    'Trusted Certificate Details': [
        { fieldName: getTrustedCertificateDisplayName('certificateName'), fieldValue: trustedCertificate.certificateName },
        { fieldName: getTrustedCertificateDisplayName('serialNumber'), fieldValue: trustedCertificate.serialNumber },
        { fieldName: getTrustedCertificateDisplayName('thumbprint'), fieldValue: trustedCertificate.thumbprint },
        { fieldName: getTrustedCertificateDisplayName('thumbprint256'), fieldValue: trustedCertificate.thumbprint256 },
        { fieldName: getTrustedCertificateDisplayName('startDate'), fieldValue: trustedCertificate.startDate },
        { fieldName: getTrustedCertificateDisplayName('endDate'), fieldValue: trustedCertificate.endDate },
        {
            fieldName: getTrustedCertificateDisplayName('issuer'),
            fieldValue: getTrustedCertificateItemInfoValues(trustedCertificate.issuer)
        },
        {
            fieldName: getTrustedCertificateDisplayName('subject'),
            fieldValue: getTrustedCertificateItemInfoValues(trustedCertificate.subject)
        },
    ],
});

export const getTrustedCertificateDetailsTabs = (trustedCertificate: TrustedCertificate): Tab[] => [
    {
        tabTitle: 'Trusted Certificate Details',
        tabSections: [{ sectionItems: getTrustedCertificateDetailsSectionItems(trustedCertificate)['Trusted Certificate Details'] }]
    }
].filter(el => el);

export const getTrustedCertificatePendingChangesTabs = (changeControl: ChangeControl): Tab[] => [
    {
        tabTitle: DIALOG_TABS.CHANGE_DETAILS,
        tabSections: [{
            sectionItems: [
                { fieldName: getTrustedCertificateDisplayName('changeID'), fieldValue: changeControl.changeID },
                { fieldName: getTrustedCertificateDisplayName('objectType'), fieldValue: changeControl.objectType },
                { fieldName: getTrustedCertificateDisplayName('operation'), fieldValue: changeControl.operation },
                { fieldName: getTrustedCertificateDisplayName('status'), fieldValue: changeControl.status },
                { fieldName: getTrustedCertificateDisplayName('changer'), fieldValue: changeControl.changer },
                { fieldName: getTrustedCertificateDisplayName('dateChanged'), fieldValue: changeControl.dateChanged },
                { fieldName: getTrustedCertificateDisplayName('changerComments'), fieldValue: changeControl.changerComments },
                { fieldName: getTrustedCertificateDisplayName('approver'), fieldValue: changeControl.approver },
                { fieldName: getTrustedCertificateDisplayName('approverComments'), fieldValue: changeControl.approverComments },
            ],
        }]
    },
    changeControl.certificateBefore && changeControl.operation !== CHANGE_OPERATION.CREATE && {
        tabTitle: DIALOG_TABS.BEFORE_CHANGES,
        tabSections: [{ sectionItems: getTrustedCertificateDetailsSectionItems(changeControl.certificateBefore)['Trusted Certificate Details'] }]
    },
    changeControl.operation !== CHANGE_OPERATION.DELETE &&
    {
        tabTitle: DIALOG_TABS.AFTER_CHANGES,
        tabSections: [{ sectionItems: getTrustedCertificateDetailsSectionItems(changeControl.trustedCertificateLog)['Trusted Certificate Details'] }]
    }
].filter(el => el);
