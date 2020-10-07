import { Component, OnInit, Inject } from '@angular/core';
import { ErrorMessage, getErrorsMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { DIALOG_TABS } from 'src/app/core/constants/dialog-tabs';
import { CHANGE_STATUS } from '../../models/changeControl/change-status';
import { Tab, DetailsDialogData } from '../details-dialog/details-dialog-data.model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { get } from 'lodash';
import { AuthService } from 'src/app/core/auth/auth.service';

@Component({
  selector: 'app-approving-dialog',
  templateUrl: './approving-dialog.component.html',
  styleUrls: ['./approving-dialog.component.scss']
})
export class ApprovingDialogComponent implements OnInit {

  getErrorsMessage = getErrorsMessage;

  dialogTabs = DIALOG_TABS;
  changeStatus = CHANGE_STATUS;

  isLoading = false;
  errorMessage: ErrorMessage;

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
    private authService: AuthService
  ) {
    this.tabs = this.data.tabs || [];
    this.data.yesCaption = this.data.yesCaption || 'Close';

    this.approveAction = get(this.data, 'actionData.approveAction');

    this.changeId = get(this.data, 'actionData.changeID', '');
    this.changer = get(this.data, 'actionData.changer');

    this.errorMessage = get(this.data, 'actionData.errorMessage', {});

    this.displayName = this.data.displayName;
  }

  ngOnInit() {
  }

  approvingAction(status) {
    this.isLoading = true;
    this.errorMessage = null;
    this.approveAction({ changeID: this.changeId, status, approverComments: this.approverComments })
      .subscribe(() => {
        this.isLoading = false;
        this.dialog.close({ refreshList: true, status });
      },
        (error) => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
  }

  getBeforeTab = (tab: Tab) => get(tab, 'tabTitle') === DIALOG_TABS.DIFFERENCES
    && this.tabs.find(el => el.tabTitle === DIALOG_TABS.BEFORE_CHANGES)

}
