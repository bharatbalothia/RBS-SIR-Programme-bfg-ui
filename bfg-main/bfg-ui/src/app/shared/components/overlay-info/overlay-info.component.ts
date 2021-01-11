import { ConnectionPositionPair } from '@angular/cdk/overlay';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-overlay-info',
  templateUrl: './overlay-info.component.html',
  styleUrls: ['./overlay-info.component.scss']
})
export class OverlayInfoComponent implements OnInit {

  isOpen = false;

  @Input() buttonName: string;
  @Input() overlayData: string | [];

  positions = [
    new ConnectionPositionPair({
      originX: 'end',
      originY: 'top'
    }, {
      overlayX: 'end',
      overlayY: 'bottom'
    },
      0,
      0)

  ];

  constructor() { }

  ngOnInit(): void {
  }

  backdropClick = () => this.isOpen = false;

}
