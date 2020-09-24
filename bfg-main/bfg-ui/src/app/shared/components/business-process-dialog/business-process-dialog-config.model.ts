import { MatDialogConfig } from '@angular/material/dialog';
import { DetailsDialogData } from '../details-dialog/details-dialog-data.model';

export class BusinessProcessDialogConfig extends MatDialogConfig<DetailsDialogData> {
    constructor(data: DetailsDialogData) {
        super();
        this.width = '100%';
        this.maxHeight = '100vh';
        this.disableClose = false;
        this.data = data;
        if (data.isDragable) {
            this.disableClose = true;
            this.hasBackdrop = false;
            this.panelClass = 'dragable-dialog-wrapper-container';
        }
    }
}
