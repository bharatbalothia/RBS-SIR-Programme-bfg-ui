import { Component, OnInit, Inject, OnDestroy } from '@angular/core';
import { DetailsDialogData, Tab } from './details-dialog-data.model';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { isUndefined, get } from 'lodash';
import { Subscription } from 'rxjs';
import { NotificationService } from '../../services/notification.service';
import { AutoRefreshService } from '../../services/autorefresh.service';

@Component({
  selector: 'app-details-dialog',
  templateUrl: './details-dialog.component.html',
  styleUrls: ['./details-dialog.component.scss']
})
export class DetailsDialogComponent implements OnInit, OnDestroy {

  displayName: (fieldName: string) => string;

  displayedColumns: string[] = ['fieldName', 'fieldValue'];

  tabs: Tab[];
  actions;

  getData: () => Promise<any>;
  getTabs: (data: any) => Tab[];

  isLoading: boolean;
  isLoadingSubscription: Subscription;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private notificationService: NotificationService,
    private autoRefreshService: AutoRefreshService
  ) {
    this.data.yesCaption = this.data.yesCaption || 'Close';
    this.displayName = this.data.displayName;

    this.getData = this.data.getData;
    this.getTabs = this.data.getTabs;
    this.actions = get(this.data, 'actionData.actions');

    if (get(this.data, 'actionData.errorMessage')) {
      this.notificationService.showErrorWithWarningMessage(get(this.data, 'actionData.errorMessage'));
    }

    if (this.data.parentLoading) {
      this.isLoadingSubscription = this.data.parentLoading.subscribe((evt: boolean) => this.isLoading = evt);
    }
  }

  ngOnInit() {
    this.autoRefreshService.shouldAutoRefresh.subscribe(value => value && this.getTabsData());
    if (!this.data.data) {
      this.getTabsData();
    }
    else {
      this.tabs = this.data.getTabs(this.data.data);
      this.updateSections();
    }
  }

  getTabsData = () => {
    if (this.getData) {
      this.getData().then((data: any) => {
        if (this.data.getTitle) {
          this.data.title = this.data.getTitle(data);
        }
        this.tabs = this.data.getTabs(data);
        this.updateSections();
      });
    }
  }

  ngOnDestroy() {
    if (this.isLoadingSubscription) {
      this.isLoadingSubscription.unsubscribe();
    }
  }

  updateSections() {
    this.tabs.forEach((tab, index) => {
      if (tab.tabSections) {
        tab.tabSections.forEach(section => section.sectionItems = section.sectionItems
          .filter(item => !isUndefined(item.fieldValue)));
      }
      this.tabs[index] = tab;
    });
  }

}
