import { Component, OnInit, Inject } from '@angular/core';
import { DIALOG_TABS } from 'src/app/core/constants/dialog-tabs';
import { CHANGE_STATUS } from '../../models/changeControl/change-status';
import { Tab, DetailsDialogData } from '../details-dialog/details-dialog-data.model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { get } from 'lodash';
import { NotificationService } from '../../services/NotificationService';

@Component({
  selector: 'app-approving-dialog',
  templateUrl: './approving-dialog.component.html',
  styleUrls: ['./approving-dialog.component.scss']
})
export class ApprovingDialogComponent implements OnInit {

  dialogTabs = DIALOG_TABS;
  changeStatus = CHANGE_STATUS;

  isLoading = false;
  hasErrors = false;

  displayedColumns: string[] = ['fieldName', 'fieldValueBefore', 'fieldValue'];
  tabs: Tab[];

  changeId: string;
  changer: string;
  approverComments: string;

  displayName: (fieldName: string) => string;

  approveAction: (params: { changeID: string, status: string, approverComments: string }) => any;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private dialog: MatDialogRef<ApprovingDialogComponent>,
    private notificationService: NotificationService
  ) {
    this.tabs = this.data.tabs || [];
    this.data.yesCaption = this.data.yesCaption || 'Close';

    this.approveAction = get(this.data, 'actionData.approveAction');

    this.changeId = get(this.data, 'actionData.changeID', '');
    this.changer = get(this.data, 'actionData.changer');

    const errorMessage = get(this.data, 'actionData.errorMessage');

    if (errorMessage) {
      this.hasErrors = (errorMessage.message || errorMessage.errors) ? true : false;
      this.notificationService.showErrorWithWarningMessage(errorMessage);
    }

    this.displayName = this.data.displayName;
  }

  ngOnInit() {
  }

  approvingAction(status) {
    this.isLoading = true;
    this.hasErrors = false;
    this.approveAction({ changeID: this.changeId, status, approverComments: this.approverComments })
      .subscribe((data) => {
        this.isLoading = false;
        const warnings = get(data, 'routingRules.warnings');
        if (warnings) {
          this.notificationService.showWarningMessage({
            code: null,
            message: null,
            warnings
          });
        }
        this.dialog.close({ refreshList: true, status });
      },
        error => {
          this.isLoading = false;
          this.hasErrors = true;
        });
  }

  getBeforeTab = (tab: Tab) => get(tab, 'tabTitle') === DIALOG_TABS.DIFFERENCES
    && this.tabs.find(el => el.tabTitle === DIALOG_TABS.BEFORE_CHANGES)

}
