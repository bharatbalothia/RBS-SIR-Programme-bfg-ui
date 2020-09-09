export interface TransactionCriteriaData {
    direction: string[];
    trxStatus: {service: string, outbound: boolean, label: string, title: string, status: number}[];
    type: string[];
    entity: string[];
}
