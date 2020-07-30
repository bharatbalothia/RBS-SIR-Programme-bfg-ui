import { Component, OnInit, Inject } from '@angular/core';
import { ErrorMessage, getErrorsMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { Tab, DetailsDialogData } from '../details-dialog/details-dialog-data.model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { get, isUndefined } from 'lodash';

@Component({
  selector: 'app-delete-dialog',
  templateUrl: './delete-dialog.component.html',
  styleUrls: ['./delete-dialog.component.scss']
})
export class DeleteDialogComponent implements OnInit {

  getErrorsMessage = getErrorsMessage;

  displayedColumns: string[] = ['fieldName', 'fieldValue'];
  tabs: Tab[] = [];

  isLoading = false;
  errorMessage: ErrorMessage;

  id: string;
  changerComments: string;

  displayName: (fieldName: string) => string;
  deleteAction: (id: string, changerComments: string) => any;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private dialog: MatDialogRef<DeleteDialogComponent>,
  ) {
    this.data.tabs = this.data.tabs || [];
    this.data.yesCaption = this.data.yesCaption || 'Close';

    this.id = get(this.data, 'actionData.id');

    this.displayName = this.data.displayName;
    this.deleteAction = get(this.data, 'actionData.deleteAction');
  }

  ngOnInit() {
    this.updateSections();
  }

  updateSections() {
    this.data.tabs.forEach((tab, index) => {
      if (tab.tabSections) {
        tab.tabSections.forEach(section => section.sectionItems = section.sectionItems
          .filter(item => !isUndefined(item.fieldValue)));
      }
      this.tabs[index] = tab;
    });
  }

  deletingAction() {
    this.isLoading = true;
    this.errorMessage = null;
    this.deleteAction(this.id, this.changerComments)
      .subscribe(() => {
        this.isLoading = false;
        this.dialog.close({ refreshList: true });
      },
        (error) => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
  }

}
