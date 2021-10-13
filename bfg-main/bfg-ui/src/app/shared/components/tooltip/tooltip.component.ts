import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-tooltip',
  templateUrl: './tooltip.component.html',
  styleUrls: ['./tooltip.component.scss']
})
export class TooltipComponent implements OnInit {

  @Input() tip: string;
  @Input() position = 'after';

  constructor() { }

  ngOnInit(): void {
  }

}
