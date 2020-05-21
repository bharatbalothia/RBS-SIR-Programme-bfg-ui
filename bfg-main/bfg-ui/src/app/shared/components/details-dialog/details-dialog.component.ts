import { Component, OnInit, Inject, Pipe } from '@angular/core';
import { DetailsDialogData, Section } from './details-dialog-data.model';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-details-dialog',
  templateUrl: './details-dialog.component.html',
  styleUrls: ['./details-dialog.component.scss']
})
export class DetailsDialogComponent implements OnInit {

  displayedColumns: string[] = ['fieldName', 'fieldValue'];
  dataSources = [];

  body: string;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData
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

}
