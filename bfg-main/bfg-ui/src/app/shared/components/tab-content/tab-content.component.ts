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

  displayedColumns: string[] = ['fieldName', 'fieldValue'];

  constructor() { }

  ngOnInit(): void {
    if (this.tab.tabSections) {
      this.tab.tabSections.filter(el => el).forEach(section => section.sectionItems = section.sectionItems
        .filter(item => this.isDifferencesTab && !isUndefined(item.fieldValue)));
    }
  }

  isDifferencesTab = (tabTitle: string) => tabTitle === DIALOG_TABS.DIFFERENCES;

  getBeforeValue = (tabSectionTitle: string, fieldName: string) => {
    const tableObject = get(this.tab, 'tableObject', null);
    const tabSections = get(this.tab, 'tabSections', []);
    const sectionItems = get(tabSections.find(el => el.sectionTitle === tabSectionTitle), 'sectionItems', []);
    return tableObject && tableObject.tableTitle === tabSectionTitle
      ? this.tab.tableObject.tableDataSource.map(el => this.tab.tableObject.formatRow ? this.tab.tableObject.formatRow(el) : el)
      : get(sectionItems.find(el => el.fieldName === fieldName), 'fieldValue', null);
  }

  getClickAction = (fieldName) => get(this.actions, fieldName, (e) => e)();

  isTableCellHasAction = (columnName) => get(this.tab, `tableObject.tableActions.${columnName}`, false);

  getTableCellClickAction = (columnName, value) => get(this.tab, `tableObject.tableActions.${columnName}`, (e) => e)(value);
}
