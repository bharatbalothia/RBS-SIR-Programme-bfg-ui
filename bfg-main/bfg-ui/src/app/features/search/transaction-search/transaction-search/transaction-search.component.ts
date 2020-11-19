import { Component, OnInit, ViewChild } from '@angular/core';
import { get } from 'lodash';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { take } from 'rxjs/operators';
import { TransactionsWithPagination } from 'src/app/shared/models/transaction/transactions-with-pagination.model';
import { getApiErrorMessage, ErrorMessage } from 'src/app/core/utils/error-template';
import * as moment from 'moment';
import { FormGroup, FormBuilder, AbstractControl } from '@angular/forms';
import { TransactionCriteriaData } from 'src/app/shared/models/transaction/transaction-criteria.model';
import { getTransactionSearchDisplayName } from '../transaction-search-display-names';
import { MatTableDataSource } from '@angular/material/table';
import { Transaction } from 'src/app/shared/models/transaction/transaction.model';
import { TransactionService } from 'src/app/shared/models/transaction/transaction.service';
import { getDirectionStringValue } from 'src/app/shared/models/file/file-directions';
import { TransactionTableComponent } from 'src/app/shared/components/transaction-table/transaction-table.component';
import { ActivatedRoute } from '@angular/router';
import { TooltipService } from 'src/app/shared/components/tooltip/tooltip.service';

@Component({
  selector: 'app-transaction-search',
  templateUrl: './transaction-search.component.html',
  styleUrls: ['./transaction-search.component.scss']
})
export class TransactionSearchComponent implements OnInit {

  getTransactionSearchDisplayName = getTransactionSearchDisplayName;

  isLinear = true;

  errorMessage: ErrorMessage;
  isLoading = false;

  @ViewChild(TransactionTableComponent)
  transactionTableComponent: TransactionTableComponent;

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

  constructor(
    private formBuilder: FormBuilder,
    private transactionService: TransactionService,
    private activatedRoute: ActivatedRoute,
    private toolTip: TooltipService
  ) {
    this.activatedRoute.queryParams.subscribe(params => {
      if (params.startDate) {
        this.defaultSelectedData = {
          from: {
            startDate: moment(params.startDate).hours(0).minutes(0).seconds(0),
            endDate: moment().hours(0).minutes(0).seconds(0),
          },
          to: null
        };
      }
    });
  }

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
      this.transactionTableComponent.autoRefreshChange(true);
    }
    else {
      this.transactionTableComponent.autoRefreshChange(false);
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

  getTooltip(step: string, field: string): string {
    const toolTip = this.toolTip.getTooltip({
      type: 'sct-search',
      qualifier: step,
      mode: 'search',
      fieldName: field
    });
    return toolTip.length > 0 ? toolTip : this.getTransactionSearchDisplayName(field);
  }

}
