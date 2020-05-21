import { Component, OnInit, Inject } from '@angular/core';
import { DetailsDialogData } from './details-dialog-data.model';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-details-dialog',
  templateUrl: './details-dialog.component.html',
  styleUrls: ['./details-dialog.component.scss']
})
export class DetailsDialogComponent implements OnInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData
  ) {
    this.data.sections = this.data.sections || [];
    this.data.yesCaption = this.data.yesCaption || 'Close';
  }

  ngOnInit() {
  }

}
