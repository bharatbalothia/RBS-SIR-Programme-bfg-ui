import { TrustedCertificate } from 'src/app/shared/models/trustedCertificate/trusted-certificate.model';
import { Tab } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { DIALOG_TABS } from 'src/app/core/constants/dialog-tabs';
import { ChangeControl } from 'src/app/shared/models/changeControl/change-control.model';
import { CHANGE_OPERATION } from 'src/app/shared/models/changeControl/change-operation';
import { get } from 'lodash';
import { formatDate } from '@angular/common';

export const TRUSTED_CERTIFICATE_DISPLAY_NAMES = {
    name: 'Name',
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
    changerNotes: 'Changer Notes',
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
    valid: 'Validity'
};

export const getTrustedCertificateDisplayName = (key: string) => TRUSTED_CERTIFICATE_DISPLAY_NAMES[key] || key;

export const getTrustedCertificateItemInfoValues = (item) => item && Object.keys(item).map(key => `${getTrustedCertificateDisplayName(key)}: ${item[key]}`).sort();

export const getValidityLabel = (value) => value === true ? 'Certificate is valid' : 'Certificate is not valid';

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
        (get(trustedCertificate, 'valid', null)) !== null && { fieldName: 'valid', fieldValue: getValidityLabel(trustedCertificate.valid) },
        (get(trustedCertificate, 'authChainReport') || []).length !== 0 && {
            fieldName: 'authChainReport',
            fieldValue: trustedCertificate.authChainReport.map(el => getTrustedCertificateItemInfoValues(el).join(',\n'))
        },
    ],
});

export const getTrustedCertificateDetailsTabs = (trustedCertificate: TrustedCertificate): Tab[] => [
    {
        tabTitle: 'Trusted Certificate Details',
        tabSections: [{ sectionItems: getTrustedCertificateDetailsSectionItems(trustedCertificate)['Trusted Certificate Details'] }]
    }
].filter(el => el);

export const getTrustedCertificatePendingChangesTabs = (changeControl: ChangeControl, isApprovingAction?: boolean): Tab[] => [
    {
        tabTitle: DIALOG_TABS.CHANGE_DETAILS,
        tabSections: [{
            sectionItems: [
                { fieldName: 'changeID', fieldValue: changeControl.changeID },
                { fieldName: 'objectType', fieldValue: changeControl.objectType },
                { fieldName: 'operation', fieldValue: changeControl.operation },
                { fieldName: 'status', fieldValue: changeControl.status },
                { fieldName: 'changer', fieldValue: changeControl.changer },
                { fieldName: 'dateChanged', fieldValue: formatDate(changeControl.dateChanged, 'yyyy-MM-dd HH:mm:ss', 'en-GB') },
                { fieldName: 'changerNotes', fieldValue: changeControl.changerComments },
                !isApprovingAction && { fieldName: 'Approver Notes', fieldValue: changeControl.approverComments },
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
