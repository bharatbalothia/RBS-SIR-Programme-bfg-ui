import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DragableDialogData } from './dragable-dialog-data.model';

@Component({
  selector: 'app-dragable-dialog',
  templateUrl: './dragable-dialog.component.html',
  styleUrls: ['./dragable-dialog.component.scss']
})
export class DragableDialogComponent implements OnInit{

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DragableDialogData
  ) { }

  ngOnInit(): void {
  }

}
