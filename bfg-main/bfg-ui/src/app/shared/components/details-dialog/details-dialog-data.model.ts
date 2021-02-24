import { EventEmitter } from '@angular/core';

export interface DetailsDialogData {
  title: string;
  tabs?: Tab[];
  yesCaption?: string;
  actionData?: any;
  parentLoading?: EventEmitter<boolean>;
  displayName?: (fieldName: string) => string;
  tooltip?: string;
}

export interface Tab {
  tabTitle: string;
  tabSections: Section[];
  tableObject?: Table;
  noContentLabel?: { label: string, icon?: string };
}

export interface Section {
  sectionTitle?: string;
  sectionItems: SectionItem[];
}

export interface Table {
  tableColumns: string[];
  tableDataSource: any[];
  tableTitle?: string;
  formatRow?: (el: any) => string;
}

export interface TableActions {
  [key: string]: (e) => any;
}

export interface SectionItem {
  fieldName: string | { label: string, nestedLabel: any };
  fieldValue: any;
  shouldDisplayValueUpperCase?: boolean;
}

export class IconValue {
  icon: string;
  value: string;

  constructor(icon: string, value: string) {
    this.icon = icon;
    this.value = value;
  }
}


