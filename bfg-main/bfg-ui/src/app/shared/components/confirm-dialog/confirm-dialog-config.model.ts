import { ConfirmDialogData } from './confirm-dialog-data.model';
import { MatDialogConfig } from '@angular/material/dialog';

export class ConfirmDialogConfig extends MatDialogConfig<ConfirmDialogData> {
    constructor(data: ConfirmDialogData) {
        super();
        this.disableClose = true;
        this.data = data;
    }
}
