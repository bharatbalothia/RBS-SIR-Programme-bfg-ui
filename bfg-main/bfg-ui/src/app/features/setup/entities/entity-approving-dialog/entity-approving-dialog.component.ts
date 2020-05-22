import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Section, DetailsDialogData } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { CHANGE_STATUS } from 'src/app/shared/entity/change-status';
import { EntityService } from 'src/app/shared/entity/entity.service';
import { getApiErrorMessage, ErrorMessage } from 'src/app/core/utils/error-template';
import { get } from 'lodash';

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
  dataSources = [];

  changeId: string;
  approverComments: string;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private dialog: MatDialogRef<EntityApprovingDialogComponent>,
    private entityService: EntityService,
  ) {
    this.data.sections = this.data.sections || [];
    this.data.yesCaption = this.data.yesCaption || 'Close';

    this.changeId = get(this.data, 'actionData.changeID', '');
  }

  ngOnInit() {
    this.updateSections();
  }

  updateSections() {
    this.data.sections.forEach((section: Section, index) => (this.dataSources[index] = section));
  }

  entityApprovingAction(status) {
    this.isLoading = true;
    this.errorMessage = null;
    this.entityService.resolveChange({ changeID: this.changeId, status, approverComments: this.approverComments })
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
