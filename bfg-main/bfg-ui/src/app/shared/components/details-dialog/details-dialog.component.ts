import { Component, OnInit, Inject, OnDestroy } from '@angular/core';
import { DetailsDialogData, Tab } from './details-dialog-data.model';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { isUndefined, get } from 'lodash';
import { ErrorMessage } from 'src/app/core/utils/error-template';
import { Subscription } from 'rxjs';

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
  errorMessage: ErrorMessage;
  errorSubscription: Subscription;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData
  ) {
    this.data.tabs = this.data.tabs || [];
    this.data.yesCaption = this.data.yesCaption || 'Close';
    this.displayName = this.data.displayName;

    this.actions = get(this.data, 'actionData.actions');
    if (this.data.parentError){
      this.errorSubscription = this.data.parentError.subscribe((evt: ErrorMessage) => this.errorMessage = evt);
    }
  }

  ngOnInit() {
    this.updateSections();
  }

  ngOnDestroy(){
    if (this.errorSubscription){
      this.errorSubscription.unsubscribe();
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
