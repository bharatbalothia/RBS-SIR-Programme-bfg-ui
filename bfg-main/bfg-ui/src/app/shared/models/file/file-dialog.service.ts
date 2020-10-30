import { EventEmitter, Injectable } from '@angular/core';
import { FileError } from './file-error.model';
import { getErrorDetailsTabs, getFileDetailsTabs, getFileSearchDisplayName } from 'src/app/features/search/file-search/file-search-display-names';
import { DetailsDialogConfig } from '../../components/details-dialog/details-dialog-config.model';
import { DetailsDialogComponent } from '../../components/details-dialog/details-dialog.component';
import { ErrorMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { getEntityDetailsTabs, getEntityDisplayName } from 'src/app/features/setup/entities/entity-display-names';
import { Entity } from '../entity/entity.model';
import { TransactionsDialogComponent } from 'src/app/features/search/file-search/transactions-dialog/transactions-dialog.component';
import { getTransactionDocumentInfoTabs } from 'src/app/features/search/transaction-search/transaction-search-display-names';
import { DocumentContent } from './document-content.model';
import { BusinessProcessDialogConfig } from '../../components/business-process-dialog/business-process-dialog-config.model';
import { BusinessProcessDialogComponent } from '../../components/business-process-dialog/business-process-dialog.component';
import { getBusinessProcessDisplayName } from '../business-process/business-process-display-names';
import { MatDialog } from '@angular/material/dialog';
import { File } from 'src/app/shared/models/file/file.model';
import { FileService } from './file.service';
import { EntityService } from '../entity/entity.service';

@Injectable({
    providedIn: 'root'
})
export class FileDialogService {

    errorMessageEmitters: { [id: number]: EventEmitter<ErrorMessage> } = {};
    errorMessage: ErrorMessage;
    isLoading = false;

    constructor(private fileService: FileService, private dialog: MatDialog, private entityService: EntityService) {
    }

    openFileDetailsDialog = (file: File) => {
        this.createErrorMessageEmitter(file.id);
        return this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
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
            parentError: this.errorMessageEmitters[file.id]
        })).afterClosed().subscribe(() => this.deleteErrorMessageEmitter(file.id));
    }

    openEntityDetailsDialog = (file: File) => this.entityService.getEntityById(file.entity.entityId)
        .pipe(data => this.setLoading(data))
        .subscribe((entity: Entity) => {
            this.isLoading = false;
            this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
                title: `${entity.service}: ${entity.entity}`,
                tabs: getEntityDetailsTabs(entity),
                displayName: getEntityDisplayName,
                isDragable: true,
            }));
        },
            error => {
                this.isLoading = false;
                this.errorMessage = getApiErrorMessage(error);
                this.emitErrorMessageEvent(file.id);
            })


    openErrorDetailsDialog = (file: File) => this.fileService.getErrorDetailsByCode(file.errorCode)
        .pipe(data => this.setLoading(data))
        .subscribe((data: FileError) => {
            this.isLoading = false;
            this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
                title: `${data.code}`,
                tabs: getErrorDetailsTabs(data),
                displayName: getFileSearchDisplayName,
                isDragable: true
            }));
        },
            error => {
                this.isLoading = false;
                this.errorMessage = getApiErrorMessage(error);
                this.emitErrorMessageEvent(file.id);
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

    openFileDocumentInfo = (file: File) => this.fileService.getDocumentContent(file.docID)
        .pipe(data => this.setLoading(data))
        .subscribe((data: DocumentContent) => {
            this.isLoading = false;
            this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
                title: `File Document Information`,
                tabs: getTransactionDocumentInfoTabs({ ...data, processID: file.workflowID }),
                displayName: getFileSearchDisplayName,
                isDragable: true
            }));
        },
            error => {
                this.isLoading = false;
                this.errorMessage = getApiErrorMessage(error);
                this.emitErrorMessageEvent(file.id);
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

    setLoading(data) {
        this.errorMessage = null;
        this.isLoading = true;
        return data;
    }

    emitErrorMessageEvent(id: number) {
        if (this.errorMessageEmitters[id]) {
            this.errorMessageEmitters[id].emit(this.errorMessage);
        }
    }

    createErrorMessageEmitter(id: number) {
        this.errorMessageEmitters[id] = new EventEmitter<ErrorMessage>();
    }

    deleteErrorMessageEmitter(id: number) {
        if (this.errorMessageEmitters[id]) {
            this.errorMessageEmitters[id] = null;
        }
    }

}
