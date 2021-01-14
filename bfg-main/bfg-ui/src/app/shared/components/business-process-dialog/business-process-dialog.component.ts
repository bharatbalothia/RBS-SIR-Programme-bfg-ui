import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { get } from 'lodash';
import { take } from 'rxjs/operators';
import { ERROR_MESSAGES } from 'src/app/core/constants/error-messages';
import { getBusinessProcessDetailsTabs, getBusinessProcessDisplayName, getBusinessProcessDocumentInfoTabs } from '../../models/business-process/business-process-display-names';
import { BusinessProcessDocumentContent } from '../../models/business-process/business-process-document-content.model';
import { BusinessProcessHeader } from '../../models/business-process/business-process-header.model';
import { BusinessProcess } from '../../models/business-process/business-process.model';
import { BusinessProcessService } from '../../models/business-process/business-process.service';
import { WorkflowStepWithPagination } from '../../models/business-process/workflow-step-with-pagination.model';
import { WorkflowStep } from '../../models/business-process/workflow-step.model';
import { NotificationService } from '../../services/NotificationService';
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

  isLoading = true;
  isBPHeaderLoading = false;

  currentDate = Date.now();

  bpHeader: BusinessProcessHeader;

  workflowSteps: WorkflowStepWithPagination;
  displayedColumns: string[] = [
    'stepId',
    'serviceName',
    'exeState',
    'advStatus',
    'startTime',
    'endTime',
    'docId',
  ];

  dataSource: MatTableDataSource<WorkflowStep>;

  pageIndex = 0;
  pageSize = 10;
  pageSizeOptions: number[] = [5, 10, 20, 50, 100];

  id: number;
  actions;

  wfdId: number = null;
  wfdVersion: number = null;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private dialog: MatDialog,
    private businessProcessService: BusinessProcessService,
    private notificationService: NotificationService
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

        if (!(this.wfdId && this.wfdVersion) && data.content.length > 0) {
          this.wfdId = data.content[0].wfdId;
          this.wfdVersion = data.content[0].wfdVersion;
          this.getBPHeader(this.wfdId, this.wfdVersion);
        }

      },
        error => {
          this.isLoading = false;
          this.isBPHeaderLoading = false;
        });
  }

  getBPHeader = (wfdID, wfdVersion) => {
    this.isBPHeaderLoading = true;
    this.businessProcessService.getBPHeader({ wfdID, wfdVersion })
      .subscribe((data: BusinessProcessHeader) => {
        this.isBPHeaderLoading = false;
        this.bpHeader = data;

        if (!this.workflowSteps.fullTracking) {
          this.notificationService.showWarningMessage({
            warnings: [{ warning: ERROR_MESSAGES.fullTrackingSteps }],
            code: null,
            message: null
          });
        }
      },
        error => this.isBPHeaderLoading = false
      );
  }

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
        error => this.isLoading = false
      );
  }

  openStepDocumentInfo = (step: WorkflowStep) => this.businessProcessService.getDocumentContent(step.docId)
    .pipe(data => this.setLoading(data))
    .subscribe((data: BusinessProcessDocumentContent) => {
      this.isLoading = false;
      this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
        title: `Primary Document`,
        tabs: getBusinessProcessDocumentInfoTabs({ ...data, processName: this.bpHeader.bpName, serviceName: step.serviceName }),
        displayName: getBusinessProcessDisplayName,
        isDragable: true
      }));
    },
      error => this.isLoading = false
    )

  updateTable() {
    this.dataSource = new MatTableDataSource(this.workflowSteps.content);
  }

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

  shouldShowBPContent = () => get(this.workflowSteps, 'content.length');

}
