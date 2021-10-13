import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { get, isUndefined } from 'lodash';
import { Subscription } from 'rxjs';
import { ErrorMessage } from 'src/app/core/utils/error-template';
import { AutoRefreshService } from '../../services/autorefresh.service';
import { NotificationService } from '../../services/notification.service';
import { DetailsDialogData, Tab } from '../details-dialog/details-dialog-data.model';

@Component({
  selector: 'app-delete-dialog',
  templateUrl: './delete-dialog.component.html',
  styleUrls: ['./delete-dialog.component.scss']
})
export class DeleteDialogComponent implements OnInit, OnDestroy {

  displayedColumns: string[] = ['fieldName', 'fieldValue'];
  tabs: Tab[] = [];

  isLoading = false;
  hasErrors = false;

  id: string;
  changerComments: string;

  tooltip;

  shouldHideComments = false;

  getData: () => Promise<any>;
  getTabs: (data: any) => Tab[];

  displayName: (fieldName: string) => string;
  deleteAction: (id: string, changerComments: string) => any;

  isAutoRefreshSubscription: Subscription;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private dialog: MatDialogRef<DeleteDialogComponent>,
    private notificationService: NotificationService,
    private autoRefreshService: AutoRefreshService
  ) {
    this.data.yesCaption = this.data.yesCaption || 'Close';

    this.getData = this.data.getData;
    this.getTabs = this.data.getTabs;

    this.id = get(this.data, 'actionData.id');

    this.displayName = this.data.displayName;
    this.deleteAction = get(this.data, 'actionData.deleteAction');

    this.tooltip = get(this.data, 'tooltip');
    this.shouldHideComments = get(this.data, 'actionData.shouldHideComments');

    this.getErrorMessage(get(this.data, 'actionData.errorMessage'));
  }

  ngOnInit() {
    this.isAutoRefreshSubscription = this.autoRefreshService.shouldAutoRefresh.subscribe(value => {
      if (value) {
        this.getTabsData();
      }
    });
    if (!this.data.data) {
      this.getTabsData();
    }
    else {
      this.tabs = this.data.getTabs(this.data.data);
      this.updateSections();
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
      }).catch(() => this.hasErrors = true);
    }
  }

  updateSections() {
    this.tabs.forEach((tab, index) => {
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

  getErrorMessage = (errorMessage: ErrorMessage) => {
    if (errorMessage) {
      this.hasErrors = (errorMessage.message || errorMessage.errors) ? true : false;
      this.notificationService.showErrorWithWarningMessage(errorMessage);
    }
    else {
      this.hasErrors = false;
    }
  }
}
