import { EventEmitter } from '@angular/core';
import { ErrorMessage } from 'src/app/core/utils/error-template';
import { TooltipKey } from '../tooltip/tooltip.model';

export interface DetailsDialogData {
    title: string;
    tabs?: Tab[];
    yesCaption?: string;
    actionData?: any;
    isDragable?: boolean;
    parentError?: EventEmitter<ErrorMessage>;
    parentLoading?: EventEmitter<boolean>;
    displayName?: (fieldName: string) => string;
    tooltip?: string;
}

export interface Tab {
    tabTitle: string;
    tabSections: Section[];
    tableObject?: Table;
    noContentLabel?: string;
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
    fieldName: string;
    fieldValue: any;
    shouldDisplayValueUpperCase?: boolean;
}
