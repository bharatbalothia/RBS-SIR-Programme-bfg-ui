import { ROUTING_PATHS } from '../../constants/routing-paths';

export interface MenuNode {
  name: string;
  route?: string;
  children?: MenuNode[];
}

export const MENU_DATA: MenuNode[] = [
  {
    name: 'Home',
    route: ROUTING_PATHS.HOME
  },
  {
    name: 'Monitor',
    children: [
      {
        name: 'File Monitor',
        route: ROUTING_PATHS.FILE_MONITOR
      },
      {
        name: 'Error Monitor',
        route: ROUTING_PATHS.ERROR_MONITOR
      },
    ]
  },
  {
    name: 'Search',
    children: [
      {
        name: 'File Search',
        route: ROUTING_PATHS.FILE_SEARCH
      },
      {
        name: 'SCT Transaction Search',
        route: ROUTING_PATHS.SCT_TRANSACTION_SEARCH
      },
    ]
  },
  {
    name: 'Setup',
    children: [
      {
        name: 'Entities',
        route: ROUTING_PATHS.ENTITIES
      },
      {
        name: 'Trusted Certificates',
        route: ROUTING_PATHS.TRUSTED_CERTIFICATES
      },
    ]
  },
  {
    name: 'SEPA Dashboard',
    children: [
      {
        name: 'Transactions with value',
        route: ROUTING_PATHS.TRANSACTIONS_WITH_VALUE
      },
    ]
  },
];
