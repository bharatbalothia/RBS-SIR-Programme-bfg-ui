export interface SCTTraffic {
    SCT_FILES: DateTraffic;
    SCT_TX: DateTraffic;
    SCT_PAW: DateTraffic;
}

interface DateTraffic {
    HOUR: number;
    WEEK: number;
    DAY: number;
}
