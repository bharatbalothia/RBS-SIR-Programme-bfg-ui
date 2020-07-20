import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-table-paginator',
  templateUrl: './table-paginator.component.html',
  styleUrls: ['./table-paginator.component.scss']
})
export class TablePaginatorComponent implements OnInit {

  @Input() min: number;
  @Input() max: number;
  @Input() value: number;
  @Input() length: number;
  @Input() pageIndex: number;
  @Input() pageSize: number;
  @Input() pageSizeOptions: number;

  @Input() getRows: (pageIndex: number, pageSize: number) => any;

  constructor() { }

  ngOnInit(): void {
  }

}
