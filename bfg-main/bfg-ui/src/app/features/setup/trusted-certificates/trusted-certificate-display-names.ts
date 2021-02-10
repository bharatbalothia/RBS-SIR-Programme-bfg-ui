import { TrustedCertificate } from 'src/app/shared/models/trustedCertificate/trusted-certificate.model';
import { IconValue, Tab } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { DIALOG_TABS } from 'src/app/core/constants/dialog-tabs';
import { ChangeControl } from 'src/app/shared/models/changeControl/change-control.model';
import { CHANGE_OPERATION } from 'src/app/shared/models/changeControl/change-operation';
import { formatDate } from '@angular/common';
import { entries, get } from 'lodash';

export const TRUSTED_CERTIFICATE_DISPLAY_NAMES = {
  name: 'Certificate Name',
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

export const TRUSTED_CERTIFICATE_DISPLAY_HEADERS = {
  fieldName: 'Field',
  subFieldName: '',
  fieldValue: 'Value'
};

export const getTrustedCertificateDisplayName = (key: string) => TRUSTED_CERTIFICATE_DISPLAY_NAMES[key] || key;

export const getTrustedCertificateDisplayHeader = (key: string): string => TRUSTED_CERTIFICATE_DISPLAY_HEADERS[key];

export const getTrustedCertificateItemInfoValues = (item) => item && Object.keys(item).map(key => `${getTrustedCertificateDisplayName(key)}: ${item[key]}`).sort();

export const getTrustedCertificateItemInfoValuesOrdered = (item) => item && [
  ...new Set([
    `${getTrustedCertificateDisplayName('CN')}: ${item.CN || ''}`,
    `${getTrustedCertificateDisplayName('OU')}: ${item.OU || ''}`,
    `${getTrustedCertificateDisplayName('O')}: ${item.O || ''}`,
    `${getTrustedCertificateDisplayName('L')}: ${item.L || ''}`,
    `${getTrustedCertificateDisplayName('ST')}: ${item.ST || ''}`,
    `${getTrustedCertificateDisplayName('C')}: ${item.C || ''}`,
    ...getTrustedCertificateItemInfoValues(item)
  ])];

export const getValidityLabel = (value) => value === true ? 'Certificate is valid' : 'Certificate is not valid';

export const getValidityIcon = (value) => value === true ? 'check_circle_outline' : 'warning';

const getTrustedCertificateDataSource = (trustedCertificate: TrustedCertificate) => [
  { fieldName: getTrustedCertificateDisplayName('certificateName'), fieldValue: trustedCertificate.certificateName },
  { fieldName: getTrustedCertificateDisplayName('serialNumber'), fieldValue: trustedCertificate.serialNumber },
  { fieldName: getTrustedCertificateDisplayName('thumbprint'), fieldValue: trustedCertificate.thumbprint },
  { fieldName: getTrustedCertificateDisplayName('thumbprint256'), fieldValue: trustedCertificate.thumbprint256 },
  { fieldName: getTrustedCertificateDisplayName('startDate'), fieldValue: trustedCertificate.startDate },
  { fieldName: getTrustedCertificateDisplayName('endDate'), fieldValue: trustedCertificate.endDate },
  ...getTrustedCertificateItemInfoValuesOrdered(trustedCertificate.issuer).map((value, index) => ({
    fieldName: index === 0 ? `${getTrustedCertificateDisplayName('issuer')}:` : undefined,
    subFieldName: value.split(': ')[0] || '',
    fieldValue: value.split(': ')[1] || '',
  })),
  ...getTrustedCertificateItemInfoValuesOrdered(trustedCertificate.subject).map((value, index) => ({
    fieldName: index === 0 ? `${getTrustedCertificateDisplayName('subject')}:` : undefined,
    subFieldName: value.split(': ')[0] || '',
    fieldValue: value.split(': ')[1] || '',
  })),
  ...get(trustedCertificate, 'valid', null) !== null ? [{
    fieldName: getTrustedCertificateDisplayName('valid'),
    fieldValue: new IconValue(
      getValidityIcon(trustedCertificate.valid),
      getValidityLabel(trustedCertificate.valid)
    )
  }] : [],
  ...get(trustedCertificate, 'authChainReport', null) !== null ?
    trustedCertificate.authChainReport.reduce((acc, current, outIndex, array) => {
      entries(current).forEach(([key, value], inIndex) => {
        acc.push({
          fieldName: outIndex === 0 && inIndex === 0 ? `${getTrustedCertificateDisplayName('authChainReport')}:` : undefined,
          subFieldName: getTrustedCertificateDisplayName(key),
          fieldValue: value
        });
      });
      return acc;
    }, []) : [],
];

export const getTrustedCertificateDetailsTabs = (trustedCertificate: TrustedCertificate): Tab[] => [
  {
    tabTitle: 'Trusted Certificate Details',
    tabSections: [],
    tableObject: {
      tableColumns: ['fieldName', 'subFieldName', 'fieldValue'],
      tableDataSource: getTrustedCertificateDataSource(trustedCertificate)
    }
  }
].filter(el => el);

export const getTrustedCertificatePendingChangesTabs = (changeControl: ChangeControl, isApprovingAction?: boolean): Tab[] => [
  {
    tabTitle: DIALOG_TABS.CHANGE_DETAILS,
    tabSections: [],
    tableObject: {
      tableColumns: ['fieldName', 'fieldValue'],
      tableDataSource: [
        { fieldName: getTrustedCertificateDisplayName('changeID'), fieldValue: changeControl.changeID },
        { fieldName: getTrustedCertificateDisplayName('objectType'), fieldValue: changeControl.objectType },
        { fieldName: getTrustedCertificateDisplayName('operation'), fieldValue: changeControl.operation },
        { fieldName: getTrustedCertificateDisplayName('status'), fieldValue: changeControl.status },
        { fieldName: getTrustedCertificateDisplayName('changer'), fieldValue: changeControl.changer },
        { fieldName: getTrustedCertificateDisplayName('dateChanged'), fieldValue: formatDate(changeControl.dateChanged, 'yyyy-MM-dd HH:mm:ss', 'en-GB') },
        { fieldName: getTrustedCertificateDisplayName('changerNotes'), fieldValue: changeControl.changerComments },
        ...!isApprovingAction ? [{
          fieldName: getTrustedCertificateDisplayName('Approver Notes'),
          fieldValue: changeControl.approverComments
        }] : [],
      ]
    }
  },
  changeControl.certificateBefore && changeControl.operation !== CHANGE_OPERATION.CREATE && {
    tabTitle: DIALOG_TABS.BEFORE_CHANGES,
    tabSections: [],
    tableObject: {
      tableColumns: ['fieldName', 'subFieldName', 'fieldValue'],
      tableDataSource: getTrustedCertificateDataSource(changeControl.certificateBefore)
    },
  },
  changeControl.operation !== CHANGE_OPERATION.DELETE &&
  {
    tabTitle: DIALOG_TABS.AFTER_CHANGES,
    tabSections: [],
    tableObject: {
      tableColumns: ['fieldName', 'subFieldName', 'fieldValue'],
      tableDataSource: getTrustedCertificateDataSource(changeControl.trustedCertificateLog)
    },
  }
].filter(el => el);
