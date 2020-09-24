export interface FileCriteriaData {
    fileStatus: { service: string, outbound: boolean, label: string, title: string, status: number }[];
    type: string[];
    entityID: string[];
    service: string[];
    direction: string[];
    bpstate: string[];
}
