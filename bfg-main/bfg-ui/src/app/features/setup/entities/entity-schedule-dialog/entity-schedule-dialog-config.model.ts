import { MatDialogConfig } from '@angular/material/dialog';
import { DetailsDialogData } from 'src/app/shared/components/details-dialog/details-dialog-data.model';

export class EntityScheduleDialogConfig extends MatDialogConfig<DetailsDialogData> {
    constructor(data: DetailsDialogData) {
        super();
        this.maxHeight = '100vh';
        this.disableClose = false;
        this.data = data;
    }
}
