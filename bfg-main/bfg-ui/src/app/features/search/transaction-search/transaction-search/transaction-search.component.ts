import { Component, OnInit, EventEmitter, OnDestroy } from '@angular/core';
import { get } from 'lodash';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { take } from 'rxjs/operators';
import { TransactionsWithPagination } from 'src/app/shared/models/transaction/transactions-with-pagination.model';
import { getApiErrorMessage, ErrorMessage } from 'src/app/core/utils/error-template';
import * as moment from 'moment';
import { FormGroup, FormBuilder, AbstractControl } from '@angular/forms';
import { TransactionCriteriaData } from 'src/app/shared/models/transaction/transaction-criteria.model';
import { getTransactionSearchDisplayName, getTransactionDetailsTabs } from '../transaction-search-display-names';
import { MatTableDataSource } from '@angular/material/table';
import { Transaction } from 'src/app/shared/models/transaction/transaction.model';
import { MatDialog } from '@angular/material/dialog';
import { TransactionService } from 'src/app/shared/models/transaction/transaction.service';
import { getStatusIcon } from 'src/app/core/constants/status-icon';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';
import { FileService } from 'src/app/shared/models/file/file.service';
import { Subscription, interval } from 'rxjs';
import { getDirectionStringValue } from 'src/app/shared/models/file/file-directions';
import { BusinessProcessDialogComponent } from 'src/app/shared/components/business-process-dialog/business-process-dialog.component';
import { getBusinessProcessDisplayName } from 'src/app/shared/models/business-process/business-process-display-names';
import { BusinessProcessDialogConfig } from 'src/app/shared/components/business-process-dialog/business-process-dialog-config.model';
import { FileDialogService } from 'src/app/shared/models/file/file-dialog.service';

@Component({
  selector: 'app-transaction-search',
  templateUrl: './transaction-search.component.html',
  styleUrls: ['./transaction-search.component.scss']
})
export class TransactionSearchComponent implements OnInit, OnDestroy {

  getTransactionSearchDisplayName = getTransactionSearchDisplayName;
  getTransactionStatusIcon = getStatusIcon;

  errorMessageEmitters: { [id: number]: EventEmitter<ErrorMessage> } = {};
  isLoadingEmitters: { [id: number]: EventEmitter<boolean> } = {};

  isLinear = true;

  autoRefreshing: Subscription;

  errorMessage: ErrorMessage;
  isLoading = false;

  searchingParametersFormGroup: FormGroup;
  transactionCriteriaData: TransactionCriteriaData;

  defaultSelectedData: { from: { startDate, endDate }, to: { startDate, endDate } } = {
    from: {
      startDate: moment().hours(0).minutes(0).seconds(0),
      endDate: moment().hours(0).minutes(0).seconds(0),
    },
    to: null
  };

  criteriaFilterObject = { direction: '', trxStatus: '' };

  transactions: TransactionsWithPagination;
  displayedColumns: string[] = [
    'id',
    'status',
    'transactionID',
    'type',
    'timestamp',
    'WFID',
    'settleDate',
    'settleAmount',
  ];

  dataSource: MatTableDataSource<Transaction>;

  pageIndex = 0;
  pageSize = 100;
  pageSizeOptions: number[] = [5, 10, 20, 50, 100];

  constructor(
    private formBuilder: FormBuilder,
    private transactionService: TransactionService,
    private fileService: FileService,
    private dialog: MatDialog,
    private fileDialogService: FileDialogService
  ) { }

  ngOnInit(): void {
    this.initializeSearchingParametersFormGroup();
    this.getTransactionCriteriaData();
  }

  initializeSearchingParametersFormGroup() {
    this.searchingParametersFormGroup = this.formBuilder.group({
      entity: [''],
      service: [''],
      direction: [''],
      trxStatus: [''],
      reference: [''],
      transactionID: [''],
      paymentBIC: [''],
      type: [''],
      from: [this.defaultSelectedData.from],
      to: [this.defaultSelectedData.to]
    });
    this.criteriaFilterObject = { direction: '', trxStatus: '' };
  }

  getTransactionCriteriaData = () =>
    this.transactionService.getTransactionCriteriaData(removeEmpties(this.criteriaFilterObject))
      .pipe(data => this.setLoading(data))
      .subscribe((data: TransactionCriteriaData) => {
        this.isLoading = false;
        this.transactionCriteriaData = data;
        this.persistSelectedTransactionStatus();
      },
        error => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        })

  persistSelectedTransactionStatus() {
    const control = this.searchingParametersFormGroup.controls.trxStatus;
    const initialStatus = control.value;
    const valueInData = this.transactionCriteriaData.trxStatus.find((val) => JSON.stringify(val) === JSON.stringify(initialStatus));
    if (valueInData) {
      control.setValue(valueInData);
    } else {
      control.setValue('');
    }
  }

  setLoading(data) {
    this.errorMessage = null;
    this.isLoading = true;
    return data;
  }


  getTransactionList(pageIndex: number, pageSize: number) {
    const formData = {
      ...this.searchingParametersFormGroup.value,
      from: this.convertDateToFormat(get(this.searchingParametersFormGroup.get('from'), 'value.startDate', null)),
      to: this.convertDateToFormat(get(this.searchingParametersFormGroup.get('to'), 'value.endDate', null)),
      page: pageIndex.toString(),
      size: pageSize.toString()
    };
    formData.status = formData.trxStatus.status;
    formData.trxStatus = null;

    this.transactionService.getTransactionList(removeEmpties(formData))
      .pipe(data => this.setLoading(data))
      .pipe(take(1)).subscribe((data: TransactionsWithPagination) => {
        this.isLoading = false;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.transactions = data;
        this.updateTable();
      },
        (error) => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
  }

  convertDateToFormat = (date: string) => moment(date).isValid() ? moment(date).format('YYYY-MM-DDTHH:mm:ss') : null;

  updateTable() {
    this.dataSource = new MatTableDataSource(this.transactions.content);
  }

  onStepChange(event) {
    if (event.selectedIndex === 1) {
      this.getTransactionList(this.pageIndex, this.pageSize);
      this.autoRefreshChange(true);
    }
    else {
      this.autoRefreshChange(false);
    }
  }

  resetSearchParameters = () => {
    this.initializeSearchingParametersFormGroup();
    this.getTransactionCriteriaData();
  }

  onDirectionSelect = (event) => {
    this.criteriaFilterObject.direction = event.value;
    this.getTransactionCriteriaData();
  }

  onStatusSelect = (event) => {
    this.setDirectionFromStatus(event.value);
  }

  getSearchingTableHeader(totalElements: number, pageSize: number, page: number) {
    const start = (page * pageSize) - (pageSize - 1);
    const end = Math.min(start + pageSize - 1, totalElements);
    return `Items ${start}-${end} of ${totalElements}`;
  }

  emitErrorMessageEvent(id: number) {
    if (this.errorMessageEmitters[id]) {
      this.errorMessageEmitters[id].emit(this.errorMessage);
    }
  }

  emitLoadingEvent(id: number) {
    if (this.isLoadingEmitters[id]) {
      this.isLoadingEmitters[id].emit(this.isLoading);
    }
  }

  createEmitters(id: number) {
    this.errorMessageEmitters[id] = new EventEmitter<ErrorMessage>();
    this.isLoadingEmitters[id] = new EventEmitter<boolean>();
  }

  deleteEmitters(id: number) {
    if (this.errorMessageEmitters[id]) {
      this.errorMessageEmitters[id] = null;
    }
    if (this.isLoadingEmitters[id]) {
      this.isLoadingEmitters[id] = null;
    }
  }


  setDirectionFromStatus(fromStatus) {
    if (fromStatus !== '') {
      let refreshRequired = false;
      const directionControl = this.searchingParametersFormGroup.controls.direction;
      const initialDirection = directionControl.value;

      const newDirection = this.transactionCriteriaData.direction.find((element) => {
        return element.toUpperCase() === getDirectionStringValue(fromStatus.outbound);
      }
      );

      if (newDirection && (initialDirection !== newDirection)) {
        directionControl.setValue(newDirection);
        refreshRequired = true;
        this.criteriaFilterObject.direction = newDirection;
      }
      if (refreshRequired) {
        this.getTransactionCriteriaData();
      }
    }
  }

  openTransactionDetailsDialog = (fileId: number, id: number) => {
    this.createEmitters(id);
    this.fileService.getTransactionById(fileId, id)
      .pipe(data => this.setLoading(data))
      .subscribe((data: Transaction) => {
        this.isLoading = false;
        const actions = {
          workflowID: () => this.openBusinessProcessDialog(data),
          file: () => this.openFileDetailsDialog({ id: fileId }, id)
        };
        this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
          title: `SCT Transaction - ${data.id}`,
          tabs: getTransactionDetailsTabs(data, actions),
          displayName: getTransactionSearchDisplayName,
          isDragable: true,
          actionData: {
            actions
          },
          parentError: this.errorMessageEmitters[id],
          parentLoading: this.isLoadingEmitters[id],
        })).afterClosed().subscribe(() => this.deleteEmitters(id));
      },
        error => {
          this.isLoading = false;
          this.errorMessage = getApiErrorMessage(error);
        });
  }


  openFileDetailsDialog = (file, transactionId) => {
    this.fileDialogService.errorMessageChange.subscribe(data => {
      this.errorMessage = data;
      this.emitErrorMessageEvent(transactionId);
    });
    this.fileDialogService.isLoadingChange.subscribe(data => {
      this.isLoading = data;
      this.emitLoadingEvent(transactionId);
    });
    this.fileDialogService.openFileDetailsDialog(file, true);
  }

  openBusinessProcessDialog = (transaction: Transaction) =>
    this.dialog.open(BusinessProcessDialogComponent, new BusinessProcessDialogConfig({
      title: `Business Process Detail`,
      tabs: [],
      displayName: getBusinessProcessDisplayName,
      isDragable: true,
      actionData: {
        id: transaction.workflowID,
        actions: {
        }
      },
    }))

  autoRefreshChange = (value) => {
    if (value) {
      this.autoRefreshing = interval(60000).subscribe(() => this.getTransactionList(this.pageIndex, this.pageSize));
    }
    else if (this.autoRefreshing) {
      this.autoRefreshing.unsubscribe();
    }
  }

  getDirectionIcon(direction: string) {
    return direction === 'outbound' ? 'call_made' : direction === 'inbound' ? 'call_received' : 'local_parking';
  }

  ngOnDestroy(): void {
    this.autoRefreshChange(false);
  }

  isInvalidFromDate = (date) => {
    const endDate = get(this.searchingParametersFormGroup.get('to'), 'value.endDate', null);
    return endDate && moment(endDate).isBefore(date);
  }

  isInvalidToDate = (date) => {
    const startDate = get(this.searchingParametersFormGroup.get('from'), 'value.startDate', null);
    return startDate && moment(date).isBefore(startDate);
  }

  onDateChange = (event, control: AbstractControl) => {
    if (get(event, 'target.value', null) === '') {
      control.setValue(null);
    }
  }

}
