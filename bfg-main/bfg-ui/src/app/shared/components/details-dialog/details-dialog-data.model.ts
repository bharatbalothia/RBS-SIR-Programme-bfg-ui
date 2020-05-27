export interface DetailsDialogData {
    title: string;
    sections: Section[];
    yesCaption?: string;
    actionData?: any;
}

export interface Section {
    sectionTitle: string;
    sectionItems: SectionItem[];
}

export interface SectionItem {
    fieldName: string;
    fieldValue: any;
    shouldDisplayValueUpperCase?: boolean;
}
