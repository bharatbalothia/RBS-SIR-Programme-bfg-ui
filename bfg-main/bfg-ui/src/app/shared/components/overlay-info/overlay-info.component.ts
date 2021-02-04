import { ConnectionPositionPair } from '@angular/cdk/overlay';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-overlay-info',
  templateUrl: './overlay-info.component.html',
  styleUrls: ['./overlay-info.component.scss']
})
export class OverlayInfoComponent {

  isOpen = false;

  @Input() buttonName: string;
  @Input() overlayData: string | [];
  @Input() key: string;
  @Output() insert = new EventEmitter<object>();

  positions = [
    new ConnectionPositionPair({
      originX: 'end',
      originY: 'top'
    }, {
      overlayX: 'end',
      overlayY: 'bottom'
    }, 0, 0)
  ];

  constructor() { }

  backdropClick = () => this.isOpen = false;

  insertValue() {
    this.insert.emit({
      key: this.key || '',
      value: this.overlayData
    });
  }
}
