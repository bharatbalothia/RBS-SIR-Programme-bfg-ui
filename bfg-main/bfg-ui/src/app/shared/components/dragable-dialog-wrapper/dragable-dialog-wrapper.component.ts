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

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private renderer: Renderer2,
    private dialogContainer: MatDialogContainer,
  ) { }

  ngOnInit(): void {
    this.setDialogResizable();
  }

  setDialogResizable = () => {
    // if (/msie\s|trident\//i.test(window.navigator.userAgent)) {
    const dialogContainerDirective = new AngularResizableDirective(this.dialogContainer['_elementRef'], this.renderer);
    dialogContainerDirective.ngOnInit();
    // }
  }

}
