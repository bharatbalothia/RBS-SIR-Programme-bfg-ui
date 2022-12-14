import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { getEntityDetailsTabs, getEntityDisplayName } from 'src/app/features/setup/entities/entity-display-names';
import { BusinessProcessDialogConfig } from 'src/app/shared/components/business-process-dialog/business-process-dialog-config.model';
import { BusinessProcessDialogComponent } from 'src/app/shared/components/business-process-dialog/business-process-dialog.component';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';
import { Tab } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';
import { getBusinessProcessDisplayName } from 'src/app/shared/models/business-process/business-process-display-names';
import { Entity } from 'src/app/shared/models/entity/entity.model';
import { EntityService } from 'src/app/shared/models/entity/entity.service';
import { DocumentContent } from 'src/app/shared/models/file/document-content.model';
import { FileError } from 'src/app/shared/models/file/file-error.model';
import { File } from 'src/app/shared/models/file/file.model';
import { FileService } from 'src/app/shared/models/file/file.service';
import { getErrorDetailsTabs, getFileDetailsTabs, getFileDocumentInfoTabs, getFileSearchDisplayName } from '../file-search-display-names';
import { TransactionsDialogComponent } from '../transactions-dialog/transactions-dialog.component';

@Component({
  selector: 'app-file-details',
  templateUrl: './file-details.component.html',
  styleUrls: ['./file-details.component.scss']
})
export class FileDetailsComponent implements OnInit {

  displayName = getFileSearchDisplayName;
  isLoading = false;

  tabs: Tab[];
  actions;

  fileId: number;

  constructor(
    private fileService: FileService,
    private dialog: MatDialog,
    private activatedRoute: ActivatedRoute,
    private entityService: EntityService
  ) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      this.fileId = params['fileId'];
      this.getFileById(this.fileId);
    });

  }

  getFileById(fileId: number) {
    this.fileService.getFileById(fileId).pipe(data => this.setLoading(data)).subscribe((file: File) => {
      this.isLoading = false;
      this.tabs = getFileDetailsTabs(file);
      this.actions = {
        entity: () => this.openEntityDetailsDialog(file),
        errorCode: () => this.openErrorDetailsDialog(file),
        transactionTotal: () => this.openTransactionsDialog(file),
        filename: () => this.openFileDocumentInfo(file),
        workflowID: () => this.openBusinessProcessDialog(file)
      };
    },
      error => {
        this.isLoading = false;
      });
  }

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

  openFileDocumentInfo = (file: File) => this.fileService.getDocumentContent(file.docID, file.messageID)
    .pipe(data => this.setLoading(data))
    .subscribe((data: DocumentContent) => {
      this.isLoading = false;
      this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
        title: `File Document Information`,
        data,
        getTabs: getFileDocumentInfoTabs,
        displayName: getFileSearchDisplayName,
      }));
    },
      error => {
        this.isLoading = false;
      })

  openEntityDetailsDialog = (file: File) => {
    const getEntity = () => this.entityService.getEntityById(file.entity.entityId)
      .pipe(data => this.setLoading(data)).toPromise()
      .then((data: Entity) => {
        this.isLoading = false;
        return data;
      }).finally(() => {
        this.isLoading = false;
      });

    getEntity().then((entity: Entity) => this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
      title: `${entity.service}: ${entity.entity}`,
      getTitle: (data: Entity) => `${data.service}: ${data.entity}`,
      data: entity,
      getData: getEntity,
      getTabs: (data: Entity) => getEntityDetailsTabs(data, file.service),
      displayName: getEntityDisplayName,
    })));
  }

  openBusinessProcessDialog = (file: File) =>
    this.dialog.open(BusinessProcessDialogComponent, new BusinessProcessDialogConfig({
      title: `Business Process Detail`,
      getTabs: () => [],
      displayName: getBusinessProcessDisplayName,
      actionData: {
        id: file.workflowID,
        actions: {
        }
      },
    }))

  openTransactionsDialog = (file: File) =>
    this.dialog.open(TransactionsDialogComponent, new DetailsDialogConfig({
      title: `Transactions for ${file.filename} [${file.id}]`,
      getTabs: () => [],
      displayName: getFileSearchDisplayName,
      actionData: {
        file: file,
        actions: {
          file: () => this.openFileDetailsDialog(file),
          workflowID: () => this.openBusinessProcessDialog(file)
        }
      },
    }))

  openFileDetailsDialog = (file: File) => {
    this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
      title: `File - ${file.id}`,
      data: file,
      getTabs: getFileDetailsTabs,
      displayName: getFileSearchDisplayName,
      actionData: {
        actions: this.actions
      }
    }));
  }

  openErrorDetailsDialog = (file: File) => this.fileService.getErrorDetailsByCode(file.errorCode)
    .pipe(data => this.setLoading(data))
    .subscribe((data: FileError) => {
      this.isLoading = false;
      this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
        title: `${data.code}`,
        data,
        getTabs: getErrorDetailsTabs,
        displayName: getFileSearchDisplayName,
      }));
    },
      error => {
        this.isLoading = false;
      })

}
