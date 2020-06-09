export interface Schedule {
    scheduleID: number;
    entityId: number;
    isWindow: boolean;
    timeStart: number;
    windowEnd: number;
    windowInterval: number;
    transThreshold: number;
    active: number;
    nextRun: Date;
    lastRun: Date;
}
