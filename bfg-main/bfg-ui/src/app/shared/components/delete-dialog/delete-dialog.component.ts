import { Component, OnInit, Inject } from '@angular/core';
import { Tab, DetailsDialogData } from '../details-dialog/details-dialog-data.model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { get, isUndefined } from 'lodash';
import { NotificationService } from '../../services/NotificationService';

@Component({
  selector: 'app-delete-dialog',
  templateUrl: './delete-dialog.component.html',
  styleUrls: ['./delete-dialog.component.scss']
})
export class DeleteDialogComponent implements OnInit {

  displayedColumns: string[] = ['fieldName', 'fieldValue'];
  tabs: Tab[] = [];

  isLoading = false;
  hasErrors = false;

  id: string;
  changerComments: string;

  tooltip;

  shouldHideComments = false;

  displayName: (fieldName: string) => string;
  deleteAction: (id: string, changerComments: string) => any;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private dialog: MatDialogRef<DeleteDialogComponent>,
    private notificationService: NotificationService
  ) {
    this.data.tabs = this.data.tabs || [];
    this.data.yesCaption = this.data.yesCaption || 'Close';

    this.id = get(this.data, 'actionData.id');

    const errorMessage = get(this.data, 'actionData.errorMessage');

    if (errorMessage) {
      this.hasErrors = (errorMessage.message || errorMessage.errors) ? true : false;
      this.notificationService.showErrorWithWarningMessage(errorMessage);
    }

    this.displayName = this.data.displayName;
    this.deleteAction = get(this.data, 'actionData.deleteAction');

    this.tooltip = get(this.data, 'tooltip');
    this.shouldHideComments = get(this.data, 'actionData.shouldHideComments');
  }

  ngOnInit() {
    this.updateSections();
  }

  updateSections() {
    this.data.tabs.forEach((tab, index) => {
      if (tab.tabSections) {
        tab.tabSections.forEach(section => section.sectionItems ? section.sectionItems = section.sectionItems
          .filter(item => !isUndefined(item.fieldValue)) : null);
      }
      this.tabs[index] = tab;
    });
  }

  deletingAction() {
    this.isLoading = true;
    this.hasErrors = false;
    this.deleteAction(this.id, this.changerComments)
      .subscribe(() => {
        this.isLoading = false;
        this.dialog.close({ refreshList: true });
      },
        error => {
          this.isLoading = false;
          this.hasErrors = true;
        });
  }

}
