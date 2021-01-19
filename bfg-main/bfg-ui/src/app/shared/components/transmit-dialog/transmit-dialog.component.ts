import { Component, Inject } from '@angular/core';
import { DetailsDialogData } from '../details-dialog/details-dialog-data.model';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material/dialog';
import { get } from 'lodash';
import { ENTITY_TRANSMIT_FILE_TYPE } from '../../models/entity/entity-constants';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { ConfirmDialogConfig } from '../confirm-dialog/confirm-dialog-config.model';
import { Entity } from '../../models/entity/entity.model';
import { PasswordConfirmationDialogComponent } from '../password-confirmation-dialog/password-confirmation-dialog.component';

@Component({
  selector: 'app-transmit-dialog',
  templateUrl: './transmit-dialog.component.html',
  styleUrls: ['./transmit-dialog.component.scss']
})
export class TransmitDialogComponent {

  FILE_TYPE = ENTITY_TRANSMIT_FILE_TYPE;

  isLoading = false;

  entity: Entity;
  fileType: string = ENTITY_TRANSMIT_FILE_TYPE.ICF;

  displayName: (fieldName: string) => string;
  transmitAction: (id: string, type: string, password: string) => any;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private dialog: MatDialogRef<TransmitDialogComponent>,
    private passwordConfirmationDialog: MatDialog,
    private confirmationDialog: MatDialog,
  ) {
    this.data.yesCaption = this.data.yesCaption || 'Close';
    this.entity = get(this.data, 'actionData.entity');
    this.displayName = this.data.displayName;
    this.transmitAction = get(this.data, 'actionData.transmitAction');
  }

  transmit() {
    this.passwordConfirmationDialog.open(PasswordConfirmationDialogComponent, {
      width: '250px'
    }).afterClosed().subscribe(password => {
      if (password) {
        this.isLoading = true;
        this.transmitAction(this.entity.entityId, this.fileType, password)
          .subscribe(() => {
            this.isLoading = false;
            this.dialog.afterClosed().subscribe(() => {
              this.confirmationDialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
                title: `Entity Transmit Now`,
                text: `The Transmit Now function was successfully started for the  ${this.entity.entity} entity`,
                shouldHideYesCaption: true,
                noCaption: 'Return'
              }));
            });
            this.dialog.close();
          },
            error => this.isLoading = false
          );
      }
    });
  }

}
