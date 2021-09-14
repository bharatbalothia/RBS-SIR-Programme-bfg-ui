import { Component, OnInit, Inject, Renderer2 } from '@angular/core';
import { MatDialogContainer, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DetailsDialogData } from '../details-dialog/details-dialog-data.model';
import { AngularResizableDirective } from 'angular2-draggable';

@Component({
  selector: 'app-dragable-dialog-wrapper',
  templateUrl: './dragable-dialog-wrapper.component.html',
  styleUrls: ['./dragable-dialog-wrapper.component.scss']
})
export class DragableDialogWrapperComponent implements OnInit {

  resizableMinWidth = '400px';
  resizableMinHeight = '250px';

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private renderer: Renderer2,
    private dialogContainer: MatDialogContainer,
  ) {
    this.data.width = this.data.width || '';
  }

  ngOnInit(): void {
    this.setDialogResizable();
  }

  setDialogResizable = () => {
    const dialogElemRef = this.dialogContainer['_elementRef'];
    this.renderer.setStyle(dialogElemRef.nativeElement, 'width', this.data.width);
    this.renderer.setStyle(dialogElemRef.nativeElement, 'min-width', this.resizableMinWidth);
    this.renderer.setStyle(dialogElemRef.nativeElement, 'min-height', this.resizableMinHeight);
    const dialogContainerDirective = new AngularResizableDirective(dialogElemRef, this.renderer);
    dialogContainerDirective.ngOnInit();
  }

}
