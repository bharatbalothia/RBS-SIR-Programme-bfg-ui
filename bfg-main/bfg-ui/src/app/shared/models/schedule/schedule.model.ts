export interface Schedule {
    scheduleID: number;
    entityId: number;
    isWindow: boolean;
    timeStart: string;
    windowEnd: string;
    windowInterval: number;
    transThreshold: number;
    fileType: string;
    active: number;
    nextRun: Date;
    lastRun: Date;
}
