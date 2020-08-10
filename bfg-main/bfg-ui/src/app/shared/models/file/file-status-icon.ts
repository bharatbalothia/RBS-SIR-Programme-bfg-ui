export const FILE_STATUS_ICON = {
    GREEN: 'green-circle-with-check',
    AMBER: 'yellow-round-error',
    RED: 'cancel-red-button'
};

export const getFileStatusIcon = (status: number) => {
    if (status < 0) {
        return FILE_STATUS_ICON.RED;
    }
    else if (status === 100 || status === 200) {
        return FILE_STATUS_ICON.GREEN;
    }
    else {
        return FILE_STATUS_ICON.AMBER;
    }
}