import { Component, EventEmitter, Inject, OnInit } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { get } from 'lodash';
import { take } from 'rxjs/operators';
import { ErrorMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { getBusinessProcessDetailsTabs, getBusinessProcessDisplayName } from '../../models/business-process/business-process-display-names';
import { BusinessProcessHeader } from '../../models/business-process/business-process-header.model';
import { BusinessProcess } from '../../models/business-process/business-process.model';
import { BusinessProcessService } from '../../models/business-process/business-process.service';
import { WorkflowStepWithPagination } from '../../models/business-process/workflow-step-with-pagination.model';
import { WorkflowStep } from '../../models/business-process/workflow-step.model';
import { DetailsDialogConfig } from '../details-dialog/details-dialog-config.model';
import { DetailsDialogData } from '../details-dialog/details-dialog-data.model';
import { DetailsDialogComponent } from '../details-dialog/details-dialog.component';

@Component({
  selector: 'app-business-process-dialog',
  templateUrl: './business-process-dialog.component.html',
  styleUrls: ['./business-process-dialog.component.scss']
})
export class BusinessProcessDialogComponent implements OnInit {

  displayName: (fieldName: string) => string;

  errorMesageEmitters: { [id: number]: EventEmitter<ErrorMessage> } = {};

  isLoading = true;
  errorMessage: ErrorMessage;

  bpHeader: BusinessProcessHeader;

  workflowSteps: WorkflowStepWithPagination;
  displayedColumns: string[] = [
    'stepId',
    'serviceName',
    'exeState',
    'advStatus',
    'startTime',
    'endTime',
    'nodeExecuted',
    'statusRpt',
    'docId',
  ];

  dataSource: MatTableDataSource<WorkflowStep>;

  pageIndex = 0;
  pageSize = 100;
  pageSizeOptions: number[] = [5, 10, 20, 50, 100];

  id: number;
  actions;

  wfdId;
  wfdVersion;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private dialog: MatDialog,
    private businessProcessService: BusinessProcessService
  ) {
    this.data.yesCaption = this.data.yesCaption || 'Close';
    this.displayName = this.data.displayName;

    this.id = get(this.data, 'actionData.id');
    this.actions = this.actions = get(this.data, 'actionData.actions');
  }

  ngOnInit() {
    this.getWorkflowSteps(this.pageIndex, this.pageSize);
  }

  getWorkflowSteps(pageIndex: number, pageSize: number) {
    this.businessProcessService.getWorkflowSteps(this.id, { page: pageIndex.toString(), size: pageSize.toString() })
      .pipe(take(1))
      .pipe(data => this.setLoading(data))
      .subscribe((data: WorkflowStepWithPagination) => {
        this.isLoading = false;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.workflowSteps = data;
        this.updateTable();

        if (!(this.wfdId && this.wfdVersion)) {
          this.wfdId = data.content[0].wfdId;
          this.wfdVersion = data.content[0].wfdVersion;
          this.getBPHeader(this.wfdId, this.wfdVersion);
        }

      },
        error => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
  }

  getBPHeader = (wfdID, wfdVersion) => this.businessProcessService.getBPHeader({ wfdID, wfdVersion })
    .pipe(data => this.setLoading(data))
    .subscribe((data: BusinessProcessHeader) => {
      this.isLoading = false;
      this.bpHeader = data;
    },
      error => {
        this.isLoading = false;
        this.errorMessage = getApiErrorMessage(error);
      })

  openBPDetails = (bpRef) => {
    this.businessProcessService.getBPDetails(bpRef)
      .pipe(data => this.setLoading(data))
      .subscribe((data: BusinessProcess) => {
        this.isLoading = false;
        this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
          title: `Business Process Details`,
          tabs: getBusinessProcessDetailsTabs(data),
          displayName: getBusinessProcessDisplayName,
          isDragable: true,
          actionData: {
          },
        }));
      },
        error => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.workflowSteps.content);
  }

  setLoading(data) {
    this.errorMessage = null;
    this.isLoading = true;
    return data;
  }

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

}
