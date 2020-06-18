import { MatDialogConfig } from '@angular/material/dialog';
import { DetailsDialogData } from './details-dialog-data.model';

export class DetailsDialogConfig extends MatDialogConfig<DetailsDialogData> {
    constructor(data: DetailsDialogData) {
        super();
        this.width = '800px';
        this.maxHeight = '100vh';
        this.disableClose = false;
        this.data = data;
    }
}
