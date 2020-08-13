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
    EMAILADDRESS: 'Email Address',
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
        { fieldName: 'certificateName', fieldValue: trustedCertificate.certificateName },
        { fieldName: 'serialNumber', fieldValue: trustedCertificate.serialNumber },
        { fieldName: 'thumbprint', fieldValue: trustedCertificate.thumbprint },
        { fieldName: 'thumbprint256', fieldValue: trustedCertificate.thumbprint256 },
        { fieldName: 'startDate', fieldValue: trustedCertificate.startDate },
        { fieldName: 'endDate', fieldValue: trustedCertificate.endDate },
        {
            fieldName: 'issuer',
            fieldValue: getTrustedCertificateItemInfoValues(trustedCertificate.issuer)
        },
        {
            fieldName: 'subject',
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
                { fieldName: 'changeID', fieldValue: changeControl.changeID },
                { fieldName: 'objectType', fieldValue: changeControl.objectType },
                { fieldName: 'operation', fieldValue: changeControl.operation },
                { fieldName: 'status', fieldValue: changeControl.status },
                { fieldName: 'changer', fieldValue: changeControl.changer },
                { fieldName: 'dateChanged', fieldValue: changeControl.dateChanged },
                { fieldName: 'changerComments', fieldValue: changeControl.changerComments },
                { fieldName: 'approver', fieldValue: changeControl.approver },
                { fieldName: 'approverComments', fieldValue: changeControl.approverComments },
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
