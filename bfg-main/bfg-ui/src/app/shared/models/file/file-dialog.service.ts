import { EventEmitter, Injectable } from '@angular/core';
import { FileError } from './file-error.model';
import { getErrorDetailsTabs, getFileDetailsTabs, getFileDocumentInfoTabs, getFileSearchDisplayName } from 'src/app/features/search/file-search/file-search-display-names';
import { DetailsDialogConfig } from '../../components/details-dialog/details-dialog-config.model';
import { DetailsDialogComponent } from '../../components/details-dialog/details-dialog.component';
import { getEntityDetailsTabs, getEntityDisplayName } from 'src/app/features/setup/entities/entity-display-names';
import { Entity } from '../entity/entity.model';
import { TransactionsDialogComponent } from 'src/app/features/search/file-search/transactions-dialog/transactions-dialog.component';
import { DocumentContent } from './document-content.model';
import { BusinessProcessDialogConfig } from '../../components/business-process-dialog/business-process-dialog-config.model';
import { BusinessProcessDialogComponent } from '../../components/business-process-dialog/business-process-dialog.component';
import { getBusinessProcessDisplayName } from '../business-process/business-process-display-names';
import { MatDialog } from '@angular/material/dialog';
import { File } from 'src/app/shared/models/file/file.model';
import { FileService } from './file.service';
import { EntityService } from '../entity/entity.service';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileDialogService {

  isLoadingEmitters: { [id: number]: EventEmitter<boolean> } = {};
  isLoading = false;
  isLoadingChange: Subject<boolean> = new Subject<boolean>();

  constructor(private fileService: FileService, private dialog: MatDialog, private entityService: EntityService) {
  }

  openFileDetailsDialog = (file: File, рropagateErr?) => {
    this.createEmitters(file.id);
    this.fileService.getFileById(file.id)
      .pipe(data => this.setLoading(data, file.id, рropagateErr))
      .subscribe((fileData: File) => {
        this.isLoading = false;
        this.emitLoadingEvent(file.id);
        if (рropagateErr) {
          this.isLoadingChange.next(false);
        }
        this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
          title: `File - ${fileData.id}`,
          tabs: getFileDetailsTabs(fileData),
          displayName: getFileSearchDisplayName,
          isDragable: true,
          actionData: {
            actions: {
              entity: () => this.openEntityDetailsDialog(fileData),
              errorCode: () => this.openErrorDetailsDialog(fileData),
              transactionTotal: () => this.openTransactionsDialog(fileData),
              filename: () => this.openFileDocumentInfo(fileData),
              workflowID: () => this.openBusinessProcessDialog(fileData)
            }
          },
          parentLoading: this.isLoadingEmitters[file.id],
        })).afterClosed().subscribe(() => this.deleteEmitters(file.id));
      },
        error => {
          this.isLoading = false;
          this.emitLoadingEvent(file.id);
          if (рropagateErr) {
            this.isLoadingChange.next(false);
          }
        });
  }

  openEntityDetailsDialog = (file: File) => this.entityService.getEntityById(file.entity.entityId)
    .pipe(data => this.setLoading(data, file.id))
    .subscribe((entity: Entity) => {
      this.isLoading = false;
      this.emitLoadingEvent(file.id);
      this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
        title: `${entity.service}: ${entity.entity}`,
        tabs: getEntityDetailsTabs(file.service, entity),
        displayName: getEntityDisplayName,
        isDragable: true,
      }));
    },
      error => {
        this.isLoading = false;
        this.emitLoadingEvent(file.id);
      })


  openErrorDetailsDialog = (file: File, рropagateErr?) => this.fileService.getErrorDetailsByCode(file.errorCode)
    .pipe(data => this.setLoading(data, file.id, рropagateErr))
    .subscribe((data: FileError) => {
      this.isLoading = false;
      this.emitLoadingEvent(file.id);
      if (рropagateErr) {
        this.isLoadingChange.next(false);
      }
      this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
        title: `${data.code}`,
        tabs: getErrorDetailsTabs(data),
        displayName: getFileSearchDisplayName,
        isDragable: true
      }));
    },
      error => {
        this.isLoading = false;
        this.emitLoadingEvent(file.id);
        if (рropagateErr) {
          this.isLoadingChange.next(false);
        }
      })

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

  openFileDocumentInfo = (file: File, рropagateErr?) => this.fileService.getDocumentContent(file.docID)
    .pipe(data => this.setLoading(data, file.id, рropagateErr))
    .subscribe((data: DocumentContent) => {
      this.isLoading = false;
      if (рropagateErr) {
        this.isLoadingChange.next(false);
      }
      this.emitLoadingEvent(file.id);
      this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
        title: file.filename,
        tabs: getFileDocumentInfoTabs(data),
        displayName: getFileSearchDisplayName,
        isDragable: true
      }));
    },
      error => {
        this.isLoading = false;
        this.emitLoadingEvent(file.id);
        if (рropagateErr) {
          this.isLoadingChange.next(false);
        }
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

  setLoading(data, id, рropagateErr?) {
    this.isLoading = true;
    this.emitLoadingEvent(id);
    if (рropagateErr) {
      this.isLoadingChange.next(true);
    }
    return data;
  }

  emitLoadingEvent(id: number) {
    if (this.isLoadingEmitters[id]) {
      this.isLoadingEmitters[id].emit(this.isLoading);
    }
  }

  createEmitters(id: number) {
    this.isLoadingEmitters[id] = new EventEmitter<boolean>();
  }

  deleteEmitters(id: number) {
    if (this.isLoadingEmitters[id]) {
      this.isLoadingEmitters[id] = null;
    }
  }

}
