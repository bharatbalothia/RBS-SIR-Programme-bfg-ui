import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Section, DetailsDialogData } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { CHANGE_STATUS } from 'src/app/shared/entity/change-status';
import { EntityService } from 'src/app/shared/entity/entity.service';

@Component({
  selector: 'app-entity-approving-dialog',
  templateUrl: './entity-approving-dialog.component.html',
  styleUrls: ['./entity-approving-dialog.component.scss']
})
export class EntityApprovingDialogComponent implements OnInit {

  changeStatus = CHANGE_STATUS;

  isLoading = false;

  displayedColumns: string[] = ['fieldName', 'fieldValue'];
  dataSources = [];

  approverComments: string;

  body: string;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private dialog: MatDialogRef<EntityApprovingDialogComponent>,
    private entityService: EntityService,
  ) {
    this.data.sections = this.data.sections || [];
    this.data.yesCaption = this.data.yesCaption || 'Close';
    this.data.actionData.changeID = this.data.actionData.changeID || '';
  }

  ngOnInit() {
    this.updateSections();
  }

  updateSections() {
    this.data.sections.forEach((section: Section, index) => (this.dataSources[index] = section));
  }

  entityApprovingAction(status) {
    this.isLoading = true;
    this.entityService.resolveChange({ changeID: this.data.actionData.changeID, status, approverComments: this.approverComments })
      .subscribe(() => {
        this.isLoading = false;
        this.dialog.close({refreshList: true});
      },
      (error) => {
        this.isLoading = false;
        this.dialog.close({refreshList: false});
      });
  }

}
