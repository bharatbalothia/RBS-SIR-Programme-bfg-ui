export interface Schedule {
    scheduleId: number;
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
