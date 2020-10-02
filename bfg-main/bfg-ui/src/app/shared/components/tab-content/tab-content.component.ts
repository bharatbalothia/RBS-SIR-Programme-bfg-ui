import { Component, OnInit, Input } from '@angular/core';
import { Tab } from '../details-dialog/details-dialog-data.model';
import { DIALOG_TABS } from 'src/app/core/constants/dialog-tabs';
import { get, isUndefined } from 'lodash';

@Component({
  selector: 'app-tab-content',
  templateUrl: './tab-content.component.html',
  styleUrls: ['./tab-content.component.scss']
})
export class TabContentComponent implements OnInit {

  @Input() tab: Tab;

  @Input() displayName: (fieldName: string) => string;

  @Input() actions;

  @Input() beforeTab;

  displayedColumns: string[] = ['fieldName', 'fieldValueBefore', 'fieldValue'];

  constructor() { }

  ngOnInit(): void {
    if (this.tab.tabSections) {
      this.tab.tabSections.filter(el => el).forEach(section => section.sectionItems = section.sectionItems
        .filter(item => this.isDifferencesTab && !isUndefined(item.fieldValue)));
    }
    console.log(this.tab);

  }

  isDifferencesTab = (tabTitle: string) => tabTitle === DIALOG_TABS.DIFFERENCES;

  getBeforeValue = (tabSectionTitle: string, fieldName: string) => {
    if (this.beforeTab) {
      const tableObject = get(this.beforeTab, 'tableObject', null);
      const tabSections = get(this.beforeTab, 'tabSections', []);
      const sectionItems = get(tabSections.find(el => el.sectionTitle === tabSectionTitle), 'sectionItems', []);
      return tableObject && tableObject.tableTitle === tabSectionTitle
        ? this.beforeTab.tableObject.tableDataSource
          .map(el => this.beforeTab.tableObject.formatRow ? this.beforeTab.tableObject.formatRow(el) : el)
        : get(sectionItems.find(el => el.fieldName === fieldName), 'fieldValue', null);
    }
  }

  getClickAction = (fieldName) => get(this.actions, fieldName, (e) => e)();

  isAllTabEmpty = () => !this.tab.tableObject
    && this.tab.tabSections.filter(el => el.sectionItems && el.sectionItems.length > 0).length === 0

  getNoDataLabel = (value: string) => `No ${value ? value.toLowerCase() : 'data'} detected.`;
}
