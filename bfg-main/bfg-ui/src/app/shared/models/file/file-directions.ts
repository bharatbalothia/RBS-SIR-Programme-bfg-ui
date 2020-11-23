export const FILE_DIRECTIONS = {
    INBOUND: 'Inbound',
    OUTBOUND: 'Outbound',
};

export const getDirectionBooleanValue = (direction: string) => direction ? direction.toUpperCase() === FILE_DIRECTIONS.OUTBOUND : null;

export const getDirectionStringValue = (outbound: boolean) => outbound ? FILE_DIRECTIONS.OUTBOUND : FILE_DIRECTIONS.INBOUND;
