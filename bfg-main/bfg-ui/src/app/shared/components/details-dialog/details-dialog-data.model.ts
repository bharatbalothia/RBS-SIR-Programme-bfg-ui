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
}

export interface SectionItem {
    fieldName: string;
    fieldValue: any;
    shouldDisplayValueUpperCase?: boolean;
}
