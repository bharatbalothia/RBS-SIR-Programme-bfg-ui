import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DetailsDialogData, Tab, Section } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { CHANGE_STATUS } from 'src/app/shared/models/changeControl/change-status';
import { EntityService } from 'src/app/shared/models/entity/entity.service';
import { getApiErrorMessage, ErrorMessage, ErrorsField, getErrorsMessage } from 'src/app/core/utils/error-template';
import { get, isUndefined } from 'lodash';
import { AuthService } from 'src/app/core/auth/auth.service';
import { ENTITY_APPROVING_DIALOG_TABS } from './entity-approving-dialog-tabs';
import { getEntityDisplayName } from '../entity-display-names';

@Component({
  selector: 'app-entity-approving-dialog',
  templateUrl: './entity-approving-dialog.component.html',
  styleUrls: ['./entity-approving-dialog.component.scss']
})
export class EntityApprovingDialogComponent implements OnInit {

  getEntityDisplayName = getEntityDisplayName;
  getErrorsMessage = getErrorsMessage;

  entityApprovingDialogTabs = ENTITY_APPROVING_DIALOG_TABS;
  changeStatus = CHANGE_STATUS;

  isLoading = false;
  errorMessage: ErrorMessage;

  displayedColumns: string[] = ['fieldName', 'fieldValueBefore', 'fieldValue'];
  tabs: Tab[];

  changeId: string;
  changer: string;
  approverComments: string;

  isApproveActions: boolean;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private dialog: MatDialogRef<EntityApprovingDialogComponent>,
    private entityService: EntityService,
    private authService: AuthService
  ) {
    this.tabs = this.data.tabs || [];
    this.data.yesCaption = this.data.yesCaption || 'Close';

    this.isApproveActions = get(this.data, 'actionData.isApproveActions', false);
    this.changeId = get(this.data, 'actionData.changeID', '');
    this.changer = get(this.data, 'actionData.changer');
    this.errorMessage = this.isTheSameUser() ? { code: null, message: 'Changes should be approved by another user' } : null;
  }

  ngOnInit() {
    this.tabs.forEach((tab: Tab) => {
      if (tab.tabSections) {
        tab.tabSections.filter(el => el).forEach(section => section.sectionItems = section.sectionItems
          .filter(item => this.isDifferencesTab && !isUndefined(item.fieldValue)));
      }
    });
  }

  entityApprovingAction(status) {
    this.isLoading = true;
    this.errorMessage = null;
    this.entityService.resolveChange({ changeID: this.changeId, status, approverComments: this.approverComments })
      .subscribe(() => {
        this.isLoading = false;
        this.dialog.close({ refreshList: true, status });
      },
        (error) => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
  }

  isTheSameUser() {
    return this.authService.getUserName() === this.changer;
  }

  isDifferencesTab = (tabTitle: string) => tabTitle === ENTITY_APPROVING_DIALOG_TABS.DIFFERENCES;

  getEntityBeforeValue = (tabSectionTitle: string, fieldName: string) => {
    const tab = this.tabs.find(el => el.tabTitle === ENTITY_APPROVING_DIALOG_TABS.BEFORE_CHANGES);
    const tableObject = get(tab, 'tableObject', null);
    const tabSections = get(tab, 'tabSections', []);
    const sectionItems = get(tabSections.find(el => el.sectionTitle === tabSectionTitle), 'sectionItems', []);
    return tableObject && tableObject.tableTitle === tabSectionTitle
      ? tab.tableObject.tableDataSource.map(el => tab.tableObject.formatRow ? tab.tableObject.formatRow(el) : el)
      : get(sectionItems.find(el => el.fieldName === fieldName), 'fieldValue', null);
  }

}
