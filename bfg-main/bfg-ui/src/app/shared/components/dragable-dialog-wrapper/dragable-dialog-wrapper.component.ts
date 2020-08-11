import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DetailsDialogData } from '../details-dialog/details-dialog-data.model';

@Component({
  selector: 'app-dragable-dialog-wrapper',
  templateUrl: './dragable-dialog-wrapper.component.html',
  styleUrls: ['./dragable-dialog-wrapper.component.scss']
})
export class DragableDialogWrapperComponent implements OnInit{

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData
  ) { }

  ngOnInit(): void {
  }

}
