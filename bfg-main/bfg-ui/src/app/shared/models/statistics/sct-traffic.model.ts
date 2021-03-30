export interface SCTTraffic {
    SCT_FILES_DAY: DateTraffic;
    SCT_FILES_HOUR: DateTraffic;
    SCT_FILES_WEEK: DateTraffic;
    SCT_TX_DAY: DateTraffic;
    SCT_TX_HOUR: DateTraffic;
    SCT_TX_WEEK: DateTraffic;
    SCT_PAW_DAY: DateTraffic;
    SCT_PAW_HOUR: DateTraffic;
    SCT_PAW_WEEK: DateTraffic;
}

interface DateTraffic {
    TOTAL: number;
    OB?: number;
    IB?: number;
}
