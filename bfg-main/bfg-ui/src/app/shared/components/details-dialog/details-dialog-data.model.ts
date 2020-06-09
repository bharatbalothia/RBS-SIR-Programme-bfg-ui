export interface DetailsDialogData {
    title: string;
    tabs?: Tab[];
    yesCaption?: string;
    actionData?: any;
}

export interface Tab {
    tabTitle: string;
    tabSections: Section[];
}

export interface Section {
    sectionTitle?: string;
    sectionItems: SectionItem[];
}

export interface SectionItem {
    fieldName: string;
    fieldValue: any;
    shouldDisplayValueUpperCase?: boolean;
}
