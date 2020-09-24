export interface FileCriteriaData {
    fileStatus: { service: string, outbound: boolean, label: string, title: string, status: number }[];
    type: string[];
    entity: { entityName: string, entityId: number };
    service: string[];
    direction: string[];
    bpState: string[];
}
