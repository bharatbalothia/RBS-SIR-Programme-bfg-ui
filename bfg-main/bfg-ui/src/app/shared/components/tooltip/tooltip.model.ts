export interface TooltipKey {
    type: string;
    qualifier: string;
    fieldName: string;
    mode: string;
}

export interface TooltipContent {
    [id: string]: string;
}
