export const STATUS_ICON = {
    GREEN: 'green-circle-with-check',
    AMBER: 'yellow-round-error',
    RED: 'cancel-red-button'
};

export const getStatusIcon = (status: number) => {
    if (status < 0) {
        return STATUS_ICON.RED;
    }
    else if (status === 100 || status === 200) {
        return STATUS_ICON.GREEN;
    }
    else {
        return STATUS_ICON.AMBER;
    }
}