import { Component, OnInit, Inject, Pipe } from '@angular/core';
import { DetailsDialogData, Section, Tab } from './details-dialog-data.model';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { isNull, isUndefined } from 'lodash';

@Component({
  selector: 'app-details-dialog',
  templateUrl: './details-dialog.component.html',
  styleUrls: ['./details-dialog.component.scss']
})
export class DetailsDialogComponent implements OnInit {

  displayedColumns: string[] = ['fieldName', 'fieldValue'];
  tabs = [];

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData
  ) {
    this.data.tabs = this.data.tabs || [];
    this.data.yesCaption = this.data.yesCaption || 'Close';
  }

  ngOnInit() {
    this.updateSections();
  }

  updateSections() {
    this.data.tabs.forEach((tab: Tab, index) => {
      tab.tabSections.forEach(section => section.sectionItems = section.sectionItems
        .filter(item => !(isNull(item.fieldValue) || isUndefined(item.fieldValue))));
      this.tabs[index] = tab;
    });
  }

}
