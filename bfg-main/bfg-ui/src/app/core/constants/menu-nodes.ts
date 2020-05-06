import { routingPaths } from './routing-paths';

export interface MenuNode {
    name: string;
    route?: string;
    children?: MenuNode[];
  }

export  const MENU_DATA: MenuNode[] = [
    {
      name: 'Home',
      route: routingPaths.HOME
    },
    {
      name: 'Reports',
      route: routingPaths.REPORTS
    },
    {
      name: 'Monitor',
      children: [
        {
          name: 'File Monitor',
          route: routingPaths.FILE_MONITOR
        },
        {
          name: 'Error Monitor',
          route: routingPaths.ERROR_MONITOR
        },
      ]
    },
    {
      name: 'Search',
      children: [
        {
          name: 'File Search',
          route: routingPaths.FILE_SEARCH
        },
        {
          name: 'SCT Transaction Search',
          route: routingPaths.SCT_TRANSACTION_SEARCH
        },
      ]
    },
    {
      name: 'Setup',
      children: [
        {
          name: 'Entities',
          route: routingPaths.ENTITIES
        },
        {
          name: 'Trusted Certificates',
          route: routingPaths.TRUSTED_CERTIFICATES
        },
      ]
    },
  ];
