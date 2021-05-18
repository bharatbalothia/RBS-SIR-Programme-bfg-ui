import { MatDialogConfig } from '@angular/material/dialog';

export class EntityScheduleDialogConfig extends MatDialogConfig<any> {
    constructor(data) {
        super();
        this.maxHeight = '100vh';
        this.disableClose = false;
        this.data = data;
    }
}
