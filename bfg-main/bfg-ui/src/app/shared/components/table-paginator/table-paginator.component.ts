import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-table-paginator',
  templateUrl: './table-paginator.component.html',
  styleUrls: ['./table-paginator.component.scss']
})
export class TablePaginatorComponent {

  @Input() min: number;
  @Input() max: number;
  @Input() value: number;
  @Input() length: number;
  @Input() pageIndex: number;
  @Input() pageSize: number;
  @Input() pageSizeOptions: number;
  @Input() getRows: (pageIndex: number, pageSize: number) => any;

  @Output() pageIndexChange = new EventEmitter<number>();

  constructor() {}

  setPageIndex(index: number = 0){
    this.pageIndex = index;
    this.pageIndexChange.emit(this.pageIndex);
  }

}
