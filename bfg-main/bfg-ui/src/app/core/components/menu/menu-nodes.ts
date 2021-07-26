import { ENTITY_PERMISSIONS } from 'src/app/shared/models/entity/entity-constants';
import { TRUSTED_CERTIFICATE_PERMISSIONS } from 'src/app/shared/models/trustedCertificate/trusted-certificate-constants';
import { GLOBAL_PERMISSIONS } from '../../constants/global-permissions';
import { ROUTING_PATHS } from '../../constants/routing-paths';

export interface MenuNode {
  name: string;
  route?: string;
  children?: MenuNode[];
  permissions?: string[];
}

export const MENU_DATA: MenuNode[] = [
  {
    name: 'Home',
    route: ROUTING_PATHS.HOME,
    permissions: [GLOBAL_PERMISSIONS.HOME]
  },
  {
    name: 'Monitor',
    children: [
      {
        name: 'File Monitor',
        route: ROUTING_PATHS.FILE_MONITOR,
        permissions: [GLOBAL_PERMISSIONS.HOME]
      },
      {
        name: 'Error Monitor',
        route: ROUTING_PATHS.ERROR_MONITOR,
        permissions: [GLOBAL_PERMISSIONS.HOME]
      },
    ]
  },
  {
    name: 'Search',
    children: [
      {
        name: 'File Search',
        route: ROUTING_PATHS.FILE_SEARCH,
        permissions: [GLOBAL_PERMISSIONS.HOME]
      },
      {
        name: 'SCT Transaction Search',
        route: ROUTING_PATHS.SCT_TRANSACTION_SEARCH,
        permissions: [GLOBAL_PERMISSIONS.HOME]
      },
    ]
  },
  {
    name: 'Setup',
    children: [
      {
        name: 'Entities',
        route: ROUTING_PATHS.ENTITIES,
        permissions: [ENTITY_PERMISSIONS.VIEW]
      },
      {
        name: 'Trusted Certificates',
        route: ROUTING_PATHS.TRUSTED_CERTIFICATES,
        permissions: [TRUSTED_CERTIFICATE_PERMISSIONS.VIEW]
      },
    ]
  },
  {
    name: 'SEPA Dashboard',
    children: [
      {
        name: 'Transactions with value',
        route: ROUTING_PATHS.TRANSACTIONS_WITH_VALUE,
        permissions: [GLOBAL_PERMISSIONS.HOME]
      },
      {
        name: 'List of files',
        route: ROUTING_PATHS.LIST_OF_FILES,
        permissions: [GLOBAL_PERMISSIONS.HOME]
      },
    ]
  },
];
