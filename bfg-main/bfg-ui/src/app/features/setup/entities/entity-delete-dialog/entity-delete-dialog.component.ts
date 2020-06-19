import { Component, OnInit, Inject } from '@angular/core';
import { isUndefined, get } from 'lodash';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DetailsDialogData, Tab } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { getDisplayName } from '../display-names';
import { EntityService } from 'src/app/shared/models/entity/entity.service';
import { ErrorMessage, getApiErrorMessage, getErrorsMessage } from 'src/app/core/utils/error-template';

@Component({
  selector: 'app-entity-delete-dialog',
  templateUrl: './entity-delete-dialog.component.html',
  styleUrls: ['./entity-delete-dialog.component.scss']
})
export class EntityDeleteDialogComponent implements OnInit {

  getDisplayName = getDisplayName;
  getErrorsMessage = getErrorsMessage;

  displayedColumns: string[] = ['fieldName', 'fieldValue'];
  tabs: Tab[] = [];

  isLoading = false;
  errorMessage: ErrorMessage;

  entityId: number;
  changerComments: string;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private entityService: EntityService,
    private dialog: MatDialogRef<EntityDeleteDialogComponent>,
  ) {
    this.data.tabs = this.data.tabs || [];
    this.data.yesCaption = this.data.yesCaption || 'Close';

    this.entityId = get(this.data, 'actionData.entityId');
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

  entityDeleteAction() {
    this.isLoading = true;
    this.errorMessage = null;
    this.entityService.deleteEntity(this.entityId, this.changerComments)
      .subscribe(() => {
        this.isLoading = false;
        this.dialog.close({ refreshList: true });
      },
        (error) => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
  }

}
