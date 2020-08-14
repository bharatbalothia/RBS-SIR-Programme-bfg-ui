import { Component, OnInit, Inject } from '@angular/core';
import { DetailsDialogData, Tab } from './details-dialog-data.model';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { isUndefined, get } from 'lodash';

@Component({
  selector: 'app-details-dialog',
  templateUrl: './details-dialog.component.html',
  styleUrls: ['./details-dialog.component.scss']
})
export class DetailsDialogComponent implements OnInit {

  displayName: (fieldName: string) => string;

  displayedColumns: string[] = ['fieldName', 'fieldValue'];
  tabs: Tab[] = [];

  actions;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData
  ) {
    this.data.tabs = this.data.tabs || [];
    this.data.yesCaption = this.data.yesCaption || 'Close';
    this.displayName = this.data.displayName;

    this.actions = get(this.data, 'actionData.actions');
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

}
