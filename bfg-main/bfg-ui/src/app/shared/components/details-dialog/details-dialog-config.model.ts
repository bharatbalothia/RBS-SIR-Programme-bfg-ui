import { MatDialogConfig } from '@angular/material/dialog';
import { DetailsDialogData } from './details-dialog-data.model';

export class DetailsDialogConfig extends MatDialogConfig<DetailsDialogData> {
    constructor(data: DetailsDialogData) {
        super();
        this.width = '500px';
        this.disableClose = false;
        this.data = data;
    }
}
