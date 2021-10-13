import { Component, OnInit, Input } from '@angular/core';
import { isArray } from 'lodash';
import { IconValue } from '../details-dialog/details-dialog-data.model';

@Component({
  selector: 'app-display-table-cell',
  templateUrl: './display-table-cell.component.html',
  styleUrls: ['./display-table-cell.component.scss']
})
export class DisplayTableCellComponent implements OnInit {

  isArray = isArray;

  @Input() value;

  @Input() xml;

  @Input() icon;

  constructor() { }

  ngOnInit(): void {}

  removeEmptiesInArray = (array: any[]) => array.filter(el => el);

  isIconValue = (value: any) => value instanceof IconValue;
}
