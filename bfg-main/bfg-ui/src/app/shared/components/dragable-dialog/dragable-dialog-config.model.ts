import { MatDialogConfig } from '@angular/material/dialog';
import { DragableDialogData } from './dragable-dialog-data.model';

export class DragableDialogConfig extends MatDialogConfig<DragableDialogData> {
    constructor(data: DragableDialogData) {
        super();
        this.disableClose = true;
        this.hasBackdrop = false;
        this.panelClass = 'dragable-dialog-container';
        this.data = data;
    }
}
