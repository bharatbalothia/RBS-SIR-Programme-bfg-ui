import { MatDialogConfig } from '@angular/material/dialog';
import { DetailsDialogData } from './details-dialog-data.model';

export class DetailsDialogConfig extends MatDialogConfig<DetailsDialogData> {
    constructor(data: DetailsDialogData,) {
        super();
        this.minWidth = '800px';
        this.maxHeight = '100vh';
        this.disableClose = false;
        this.data = data;
        this.disableClose = true;
        this.hasBackdrop = false;
        this.panelClass = 'dragable-dialog-wrapper-container';
    }
}
