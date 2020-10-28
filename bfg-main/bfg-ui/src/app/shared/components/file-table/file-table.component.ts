import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { interval, Subscription } from 'rxjs';
import { getStatusIcon } from 'src/app/core/constants/status-icon';
import { ErrorMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { getErrorDetailsTabs, getFileDetailsTabs, getFileSearchDisplayName } from 'src/app/features/search/file-search/file-search-display-names';
import { TransactionsDialogComponent } from 'src/app/features/search/file-search/transactions-dialog/transactions-dialog.component';
import { getTransactionDocumentInfoTabs } from 'src/app/features/search/transaction-search/transaction-search-display-names';
import { getEntityDetailsTabs, getEntityDisplayName } from 'src/app/features/setup/entities/entity-display-names';
import { getBusinessProcessDisplayName } from '../../models/business-process/business-process-display-names';
import { Entity } from '../../models/entity/entity.model';
import { EntityService } from '../../models/entity/entity.service';
import { DocumentContent } from '../../models/file/document-content.model';
import { FileError } from '../../models/file/file-error.model';
import { File } from '../../models/file/file.model';
import { FileService } from '../../models/file/file.service';
import { FilesWithPagination } from '../../models/file/files-with-pagination.model';
import { BusinessProcessDialogConfig } from '../business-process-dialog/business-process-dialog-config.model';
import { BusinessProcessDialogComponent } from '../business-process-dialog/business-process-dialog.component';
import { DetailsDialogConfig } from '../details-dialog/details-dialog-config.model';
import { DetailsDialogComponent } from '../details-dialog/details-dialog.component';

@Component({
  selector: 'app-file-table',
  templateUrl: './file-table.component.html',
  styleUrls: ['./file-table.component.scss']
})
export class FileTableComponent implements OnInit, OnDestroy {

  getFileStatusIcon = getStatusIcon;

  @Input() getFileList: (pageIndex: number, pageSize: number) => any;
  @Input() errorMessage: ErrorMessage;
  @Input() dataSource: MatTableDataSource<File>;
  @Input() files: FilesWithPagination;
  @Input() isLoading = false;
  @Input() pageIndex;
  @Input() pageSize;
  @Input() shouldHidePaginator = false;
  @Input() shouldHideTableHeader = false;
  @Input() shouldAutoRefresh = true;

  @Output() errorMessageChange: EventEmitter<ErrorMessage> = new EventEmitter<ErrorMessage>();
  @Output() isLoadingChange: EventEmitter<boolean> = new EventEmitter<boolean>();

  errorMesageEmitters: { [id: number]: EventEmitter<ErrorMessage> } = {};

  autoRefreshing: Subscription;

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
    private dialog: MatDialog,
    private fileService: FileService,
    private entityService: EntityService
  ) { }

  ngOnInit(): void {
    this.autoRefreshChange(this.shouldAutoRefresh);
  }

  getSearchingTableHeader(totalElements: number, pageSize: number, page: number) {
    const start = (page * pageSize) - (pageSize - 1);
    const end = Math.min(start + pageSize - 1, totalElements);
    return `Items ${start}-${end} of ${totalElements}`;
  }

  setLoading(data) {
    this.errorMessage = null;
    this.errorMessageChange.emit(this.errorMessage);
    this.isLoading = true;
    this.isLoadingChange.emit(this.isLoading);
    return data;
  }

  openFileDetailsDialog = (file: File) => {
    this.createErrorMesageEmitter(file.id);
    this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
      title: `File - ${file.id}`,
      tabs: getFileDetailsTabs(file),
      displayName: getFileSearchDisplayName,
      isDragable: true,
      actionData: {
        actions: {
          entity: () => this.openEntityDetailsDialog(file),
          errorCode: () => this.openErrorDetailsDialog(file),
          transactionTotal: () => this.openTransactionsDialog(file),
          filename: () => this.openFileDocumentInfo(file),
          workflowID: () => this.openBusinessProcessDialog(file)
        }
      },
      parentError: this.errorMesageEmitters[file.id]
    })).afterClosed().subscribe(() => this.deleteErrorMesageEmitter(file.id));
  }

  openFileDocumentInfo = (file: File) => this.fileService.getDocumentContent(file.docID)
    .pipe(data => this.setLoading(data))
    .subscribe((data: DocumentContent) => {
      this.isLoading = false;
      this.isLoadingChange.emit(this.isLoading);
      this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
        title: `File Document Information`,
        tabs: getTransactionDocumentInfoTabs({ ...data, processID: file.workflowID }),
        displayName: getFileSearchDisplayName,
        isDragable: true
      }));
    },
      error => {
        this.isLoading = false;
        this.isLoadingChange.emit(this.isLoading);
        this.errorMessage = getApiErrorMessage(error);
        this.errorMessageChange.emit(this.errorMessage);
        this.emitErrorMesageEvent(file.id);
      })

  openEntityDetailsDialog = (file: File) => this.entityService.getEntityById(file.entity.entityId)
    .pipe(data => this.setLoading(data))
    .subscribe((entity: Entity) => {
      this.isLoading = false;
      this.isLoadingChange.emit(this.isLoading);
      this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
        title: `${entity.service}: ${entity.entity}`,
        tabs: getEntityDetailsTabs(entity),
        displayName: getEntityDisplayName,
        isDragable: true,

      }));
    },
      error => {
        this.isLoading = false;
        this.isLoadingChange.emit(this.isLoading);
        this.errorMessage = getApiErrorMessage(error);
        this.errorMessageChange.emit(this.errorMessage);
        this.emitErrorMesageEvent(file.id);
      })

  openBusinessProcessDialog = (file: File) =>
    this.dialog.open(BusinessProcessDialogComponent, new BusinessProcessDialogConfig({
      title: `Business Process Detail`,
      tabs: [],
      displayName: getBusinessProcessDisplayName,
      isDragable: true,
      actionData: {
        id: file.workflowID,
        actions: {
        }
      },
    }))

  openTransactionsDialog = (file: File) =>
    this.dialog.open(TransactionsDialogComponent, new DetailsDialogConfig({
      title: `Transactions for ${file.filename} [${file.id}]`,
      tabs: [],
      displayName: getFileSearchDisplayName,
      isDragable: true,
      actionData: {
        fileId: file.id,
        actions: {
          file: () => this.openFileDetailsDialog(file),
          workflowID: () => this.openBusinessProcessDialog(file)
        }
      },
    }))

  openErrorDetailsDialog = (file: File) => this.fileService.getErrorDetailsByCode(file.errorCode)
    .pipe(data => this.setLoading(data))
    .subscribe((data: FileError) => {
      this.isLoading = false;
      this.isLoadingChange.emit(this.isLoading);
      this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
        title: `${data.code}`,
        tabs: getErrorDetailsTabs(data),
        displayName: getFileSearchDisplayName,
        isDragable: true
      }));
    },
      error => {
        this.isLoading = false;
        this.isLoadingChange.emit(this.isLoading);
        this.errorMessage = getApiErrorMessage(error);
        this.errorMessageChange.emit(this.errorMessage);
      })


  createErrorMesageEmitter(id: number) {
    this.errorMesageEmitters[id] = new EventEmitter<ErrorMessage>();
  }

  deleteErrorMesageEmitter(id: number) {
    if (this.errorMesageEmitters[id]) {
      this.errorMesageEmitters[id] = null;
    }
  }

  emitErrorMesageEvent(id: number) {
    if (this.errorMesageEmitters[id]) {
      this.errorMesageEmitters[id].emit(this.errorMessage);
    }
  }

  autoRefreshChange = (value) => {
    if (value) {
      this.autoRefreshing = interval(60000).subscribe(() => this.getFileList(this.pageIndex, this.pageSize));
    }
    else if (this.autoRefreshing) {
      this.autoRefreshing.unsubscribe();
    }
  }

  ngOnDestroy(): void {
    this.autoRefreshChange(false);
  }
}
