export interface DetailsDialogData {
    title: string;
    tabs?: Tab[];
    yesCaption?: string;
    actionData?: any;
}

export interface Tab {
    tabTitle: string;
    tabSections: Section[];
    tableObject?: Table;
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

export interface SectionItem {
    fieldName: string;
    fieldValue: any;
    shouldDisplayValueUpperCase?: boolean;
}
