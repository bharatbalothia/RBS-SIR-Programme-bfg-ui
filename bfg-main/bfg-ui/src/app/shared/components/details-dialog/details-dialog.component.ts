import { Component, OnInit, Inject, OnDestroy, ElementRef, Renderer2 } from '@angular/core';
import { DetailsDialogData, Tab } from './details-dialog-data.model';
import { MatDialogContainer, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { isUndefined, get } from 'lodash';
import { Subscription } from 'rxjs';
import { NotificationService } from '../../services/notification.service';
import { AngularResizableDirective } from 'angular2-draggable';

@Component({
  selector: 'app-details-dialog',
  templateUrl: './details-dialog.component.html',
  styleUrls: ['./details-dialog.component.scss']
})
export class DetailsDialogComponent implements OnInit, OnDestroy {

  displayName: (fieldName: string) => string;

  displayedColumns: string[] = ['fieldName', 'fieldValue'];
  tabs: Tab[] = [];

  actions;

  isLoading: boolean;
  isLoadingSubscription: Subscription;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private notificationService: NotificationService,
  ) {
    this.data.tabs = this.data.tabs || [];
    this.data.yesCaption = this.data.yesCaption || 'Close';
    this.displayName = this.data.displayName;

    this.actions = get(this.data, 'actionData.actions');

    if (get(this.data, 'actionData.errorMessage')) {
      this.notificationService.showErrorWithWarningMessage(get(this.data, 'actionData.errorMessage'));
    }

    if (this.data.parentLoading) {
      this.isLoadingSubscription = this.data.parentLoading.subscribe((evt: boolean) => this.isLoading = evt);
    }
  }

  ngOnInit() {
    this.updateSections();
  }

  ngOnDestroy() {
    if (this.isLoadingSubscription) {
      this.isLoadingSubscription.unsubscribe();
    }
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

}
