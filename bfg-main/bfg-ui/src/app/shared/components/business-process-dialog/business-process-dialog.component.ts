import { Component, EventEmitter, Inject, OnInit } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { get } from 'lodash';
import { take } from 'rxjs/operators';
import { ErrorMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { BusinessProcessService } from '../../models/business-process/business-process.service';
import { WorkflowStepWithPagination } from '../../models/business-process/workflow-step-with-pagination.model';
import { WorkflowStep } from '../../models/business-process/workflow-step.model';
import { DetailsDialogData } from '../details-dialog/details-dialog-data.model';

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
