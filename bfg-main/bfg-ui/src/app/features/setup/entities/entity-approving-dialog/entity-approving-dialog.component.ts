import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Section, DetailsDialogData } from 'src/app/shared/components/details-dialog/details-dialog-data.model';

@Component({
  selector: 'app-entity-approving-dialog',
  templateUrl: './entity-approving-dialog.component.html',
  styleUrls: ['./entity-approving-dialog.component.scss']
})
export class EntityApprovingDialogComponent implements OnInit {

  isLoading = false;

  displayedColumns: string[] = ['fieldName', 'fieldValue'];
  dataSources = [];

  approverComments: string;

  body: string;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private dialog: MatDialogRef<EntityApprovingDialogComponent>,
  ) {
    this.data.sections = this.data.sections || [];
    this.data.yesCaption = this.data.yesCaption || 'Close';
  }

  ngOnInit() {
    this.updateSections();
  }

  updateSections() {
    this.data.sections.forEach((section: Section, index) => (this.dataSources[index] = section));
  }

  entityApprovingAction(status) {
    // make request with status, then change isLoading and close dialog
  }

}
