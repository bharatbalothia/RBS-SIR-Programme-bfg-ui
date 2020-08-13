export interface FileCriteriaData {
    fileStatus: {service: string, outbound: boolean, label: string, title: string, status: number}[];
    type: string[];
    entity: string[];
    service: string[];
    direction: string[];
    bpState: string[];
}
