import { Component, Inject } from '@angular/core';
import { ErrorMessage, getErrorsMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { DetailsDialogData } from '../details-dialog/details-dialog-data.model';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material/dialog';
import { get } from 'lodash';
import { ENTITY_TRANSMIT_FILE_TYPE } from '../../models/entity/entity-constants';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { ConfirmDialogConfig } from '../confirm-dialog/confirm-dialog-config.model';
import { Entity } from '../../models/entity/entity.model';

@Component({
  selector: 'app-transmit-dialog',
  templateUrl: './transmit-dialog.component.html',
  styleUrls: ['./transmit-dialog.component.scss']
})
export class TransmitDialogComponent {

  getErrorsMessage = getErrorsMessage;
  FILE_TYPE = ENTITY_TRANSMIT_FILE_TYPE;

  isLoading = false;
  errorMessage: ErrorMessage;

  entity: Entity;
  fileType: string;

  displayName: (fieldName: string) => string;
  transmitAction: (id: string, changerComments: string) => any;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private dialog: MatDialogRef<TransmitDialogComponent>,
    private confirmationDialog: MatDialog,
  ) {
    this.data.yesCaption = this.data.yesCaption || 'Close';
    this.entity = get(this.data, 'actionData.entity');
    this.displayName = this.data.displayName;
    this.transmitAction = get(this.data, 'actionData.transmitAction');
  }

  transmit() {
    this.isLoading = true;
    this.errorMessage = null;
    this.transmitAction(this.entity.entityId, this.fileType)
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
        (error) => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
  }

}
