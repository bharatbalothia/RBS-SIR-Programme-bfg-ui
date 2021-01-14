import { Component, OnInit, Inject } from '@angular/core';
import { ConfirmDialogData } from './confirm-dialog-data.model';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss']
})
export class ConfirmDialogComponent implements OnInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData
  ) {
    this.data.yesCaption = this.data.yesCaption || 'Yes';
    this.data.shouldHideYesCaption = this.data.shouldHideYesCaption || false;
    this.data.yesCaptionColor = this.data.yesCaptionColor || 'primary';
    this.data.noCaption = this.data.noCaption || 'No';
  }

  ngOnInit() {
  }

}
