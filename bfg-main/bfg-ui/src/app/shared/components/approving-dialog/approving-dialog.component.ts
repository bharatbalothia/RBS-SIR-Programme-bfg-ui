import { Component, OnInit, Inject, OnDestroy } from '@angular/core';
import { DIALOG_TABS } from 'src/app/core/constants/dialog-tabs';
import { CHANGE_STATUS } from '../../models/changeControl/change-status';
import { Tab, DetailsDialogData } from '../details-dialog/details-dialog-data.model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { get } from 'lodash';
import { NotificationService } from '../../services/notification.service';
import { AutoRefreshService } from '../../services/autorefresh.service';
import { Subscription } from 'rxjs';
import { ErrorMessage } from 'src/app/core/utils/error-template';

@Component({
  selector: 'app-approving-dialog',
  templateUrl: './approving-dialog.component.html',
  styleUrls: ['./approving-dialog.component.scss']
})
export class ApprovingDialogComponent implements OnInit, OnDestroy {

  dialogTabs = DIALOG_TABS;
  changeStatus = CHANGE_STATUS;

  isLoading = false;
  hasErrors = false;
  shouldDisableApprove = false;

  displayedColumns: string[] = ['fieldName', 'fieldValueBefore', 'fieldValue'];
  tabs: Tab[];

  getData: () => Promise<any>;
  getTabs: (data: any) => Tab[];

  changeId: string;
  changer: string;
  approverComments: string;

  displayName: (fieldName: string) => string;

  approveAction: (params: { changeID: string, status: string, approverComments: string }) => any;

  isAutoRefreshSubscription: Subscription;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private dialog: MatDialogRef<ApprovingDialogComponent>,
    private notificationService: NotificationService,
    private autoRefreshService: AutoRefreshService
  ) {
    this.data.yesCaption = this.data.yesCaption || 'Close';

    this.getData = this.data.getData;
    this.getTabs = this.data.getTabs;

    this.approveAction = get(this.data, 'actionData.approveAction');

    this.changeId = get(this.data, 'actionData.changeID', '');
    this.changer = get(this.data, 'actionData.changer');

    this.shouldDisableApprove = get(this.data, 'actionData.shouldDisableApprove');

    this.displayName = this.data.displayName;

    this.getErrorMessage(get(this.data, 'actionData.errorMessage'));

  }

  ngOnInit() {
    this.isAutoRefreshSubscription = this.autoRefreshService.shouldAutoRefresh.subscribe(value => {
      if (value) {
        this.getTabsData();
        this.getErrorMessage(get(this.data, 'actionData.errorMessage'));
      }
    });
    if (!this.data.data) {
      this.getTabsData();
    }
    else {
      this.tabs = this.data.getTabs(this.data.data);
    }
  }

  ngOnDestroy() {
    if (this.isAutoRefreshSubscription) {
      this.isAutoRefreshSubscription.unsubscribe();
    }
  }

  getTabsData = () => {
    if (this.getData) {
      this.getData().then((data: any) => {
        if (this.data.getTitle) {
          this.data.title = this.data.getTitle(data);
        }
        this.tabs = this.data.getTabs(data);
      }).catch(() => {
        this.hasErrors = true;
      });
    }
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

  getErrorMessage = (errorMessage: ErrorMessage) => {
    const warningMessage = get(this.data, 'actionData.warningMessage');
    if (warningMessage) {
      this.notificationService.showWarningText(warningMessage);
    }
    if (errorMessage) {
      this.hasErrors = (errorMessage.message || errorMessage.errors) ? true : false;
      this.notificationService.showErrorWithWarningMessage(errorMessage);
    }
    else {
      this.hasErrors = false;
    }
  }
}
