import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-display-table-cell',
  templateUrl: './display-table-cell.component.html',
  styleUrls: ['./display-table-cell.component.scss']
})
export class DisplayTableCellComponent implements OnInit {

  _array = Array;
  @Input() value;

  @Input() xml;

  @Input() icon;

  constructor() { }

  ngOnInit(): void {
  }

  removeEmptiesInArray = (array: any[]) => array.filter(el => el);
}
