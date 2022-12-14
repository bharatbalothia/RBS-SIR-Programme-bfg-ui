import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { getStatusIcon } from 'src/app/core/constants/status-icon';
import { getDirectionIcon } from 'src/app/features/search/transaction-search/transaction-search-display-names';
import { FileDialogService } from '../../models/file/file-dialog.service';
import { File } from '../../models/file/file.model';
import { FilesWithPagination } from '../../models/file/files-with-pagination.model';

@Component({
  selector: 'app-file-table',
  templateUrl: './file-table.component.html',
  styleUrls: ['./file-table.component.scss']
})
export class FileTableComponent implements OnInit {

  getFileStatusIcon = getStatusIcon;
  getDirectionIcon = getDirectionIcon;

  @Input() getFileList: (pageIndex: number, pageSize: number) => any;
  @Input() dataSource: MatTableDataSource<File>;
  @Input() files: FilesWithPagination;
  @Input() isLoading = false;
  @Input() pageIndex;
  @Input() pageSize;
  @Input() shouldHidePaginator = false;
  @Input() shouldHideTableHeader = false;

  @Output() isLoadingChange: EventEmitter<boolean> = new EventEmitter<boolean>();

  displayedColumns: string[] = [
    'status',
    'id',
    'filename',
    'reference',
    'type',
    'service',
    'timestamp',
    'WFID',
    'error',
    'transactions'
  ];

  pageSizeOptions: number[] = [5, 10, 20, 50, 100];

  constructor(
    private fileDialogService: FileDialogService,
  ) { }

  ngOnInit(): void {
    this.fileDialogService.isLoadingChange.subscribe(data => this.isLoadingChange.emit(data));
  }

  getSearchingTableHeader(totalElements: number, pageSize: number, page: number) {
    const start = (page * pageSize) - (pageSize - 1);
    const end = Math.min(start + pageSize - 1, totalElements);
    return `Items ${start}-${end} of ${totalElements}`;
  }

  openFileDetailsDialog = (file: File) => this.fileDialogService.openFileDetailsDialog(file, true);

  openFileDocumentInfo = (file: File) => this.fileDialogService.openFileDocumentInfo(file, true);

  openEntityDetailsDialog = (file: File) => this.fileDialogService.openEntityDetailsDialog(file);

  openBusinessProcessDialog = (file: File) => this.fileDialogService.openBusinessProcessDialog(file);

  openTransactionsDialog = (file: File) => this.fileDialogService.openTransactionsDialog(file);

  openErrorDetailsDialog = (file: File) => this.fileDialogService.openErrorDetailsDialog(file, true);

}
