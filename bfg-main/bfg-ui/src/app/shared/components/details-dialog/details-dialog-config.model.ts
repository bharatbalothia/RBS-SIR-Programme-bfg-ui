import { MatDialogConfig } from '@angular/material/dialog';
import { DetailsDialogData } from './details-dialog-data.model';

export class DetailsDialogConfig extends MatDialogConfig<DetailsDialogData> {
    constructor(data: DetailsDialogData,) {
        super();
        this.maxHeight = '100vh';
        this.data = data;
        this.data.width = '800px';
        this.disableClose = true;
        this.hasBackdrop = false;
        this.panelClass = 'dragable-dialog-wrapper-container';
    }
}
