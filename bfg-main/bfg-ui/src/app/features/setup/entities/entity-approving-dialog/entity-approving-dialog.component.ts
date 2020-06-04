import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DetailsDialogData, Tab } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { CHANGE_STATUS } from 'src/app/shared/entity/change-status';
import { EntityService } from 'src/app/shared/entity/entity.service';
import { getApiErrorMessage, ErrorMessage, ErrorsField } from 'src/app/core/utils/error-template';
import { get, isNull, isUndefined } from 'lodash';
import { AuthService } from 'src/app/core/auth/auth.service';

@Component({
  selector: 'app-entity-approving-dialog',
  templateUrl: './entity-approving-dialog.component.html',
  styleUrls: ['./entity-approving-dialog.component.scss']
})
export class EntityApprovingDialogComponent implements OnInit {

  changeStatus = CHANGE_STATUS;

  isLoading = false;
  errorMessage: ErrorMessage;

  displayedColumns: string[] = ['fieldName', 'fieldValue'];
  tabs = [];

  changeId: string;
  changer: string;
  approverComments: string;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private dialog: MatDialogRef<EntityApprovingDialogComponent>,
    private entityService: EntityService,
    private authService: AuthService
  ) {
    this.data.tabs = this.data.tabs || [];
    this.data.yesCaption = this.data.yesCaption || 'Close';

    this.changeId = get(this.data, 'actionData.changeID', '');
    this.changer = get(this.data, 'actionData.changer');
    this.errorMessage = this.isTheSameUser() ? { code: null, message: 'Changes should be approved by another user' } : null;
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

  getErrorsMessage = (error: ErrorsField) => Object.keys(error).map(e => error[e]);

  isTheSameUser() {
    return this.authService.getUserName() === this.changer;
  }

}
