export interface TransactionCriteriaData {
    direction: { payaway: string, directionLabel: string, index: number, direction: string }[];
    trxStatus: { service: string, outbound: boolean, label: string, title: string, status: number }[];
    type: string[];
    entity: string[];
}
