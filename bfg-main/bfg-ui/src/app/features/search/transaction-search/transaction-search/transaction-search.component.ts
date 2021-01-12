import { AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { get, isEmpty } from 'lodash';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { take } from 'rxjs/operators';
import { TransactionsWithPagination } from 'src/app/shared/models/transaction/transactions-with-pagination.model';
import { getApiErrorMessage, ErrorMessage, getErrorByField } from 'src/app/core/utils/error-template';
import * as moment from 'moment';
import { FormGroup, FormBuilder } from '@angular/forms';
import { TransactionCriteriaData } from 'src/app/shared/models/transaction/transaction-criteria.model';
import { getTransactionSearchDisplayName } from '../transaction-search-display-names';
import { MatTableDataSource } from '@angular/material/table';
import { Transaction } from 'src/app/shared/models/transaction/transaction.model';
import { TransactionService } from 'src/app/shared/models/transaction/transaction.service';
import { getDirectionStringValue } from 'src/app/shared/models/file/file-directions';
import { TransactionTableComponent } from 'src/app/shared/components/transaction-table/transaction-table.component';
import { ActivatedRoute } from '@angular/router';
import { TooltipService } from 'src/app/shared/components/tooltip/tooltip.service';
import { MatHorizontalStepper } from '@angular/material/stepper';
import { ErrorStateMatcher } from '@angular/material/core';
import { CrossFieldErrorMatcher } from 'src/app/shared/classes/CrossFieldErrorMatcher';
import { dateRangeValidator } from 'src/app/shared/models/search/validators';
import { getSearchValidationMessage } from 'src/app/shared/models/search/validation-messages';

@Component({
  selector: 'app-transaction-search',
  templateUrl: './transaction-search.component.html',
  styleUrls: ['./transaction-search.component.scss']
})
export class TransactionSearchComponent implements OnInit, AfterViewInit {

  @ViewChild(TransactionTableComponent)
  transactionTableComponent: TransactionTableComponent;

  @ViewChild('stepper') stepper: MatHorizontalStepper;

  errorMatcher: ErrorStateMatcher;
  errorMessage: ErrorMessage;

  searchingParametersFormGroup: FormGroup;
  transactionCriteriaData: TransactionCriteriaData;

  getSearchValidationMessage = getSearchValidationMessage;
  getTransactionSearchDisplayName = getTransactionSearchDisplayName;

  isLinear = true;
  isLoading = false;

  defaultSelectedData: { from: moment.Moment, to: moment.Moment } = {
    from: moment().startOf('day'),
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

  URLParams;

  constructor(
    private formBuilder: FormBuilder,
    private transactionService: TransactionService,
    private activatedRoute: ActivatedRoute,
    private toolTip: TooltipService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.errorMatcher = new CrossFieldErrorMatcher();
    this.activatedRoute.queryParams.subscribe(params => {
      this.URLParams = { ...params };
      if (params.startDate) {
        this.defaultSelectedData = {
          from: params.startDate === 'none' ? null : moment(params.startDate),
          to: null
        };
        delete this.URLParams.startDate;
      }
      this.initializeSearchingParametersFormGroup();
      this.getTransactionCriteriaData();
    });
  }

  ngAfterViewInit() {
    if (!this.isURLParamsEmpty()) {
      this.stepper.next();
      this.cdr.detectChanges();
    }
  }

  isURLParamsEmpty = () => isEmpty(this.URLParams);

  getErrorByField = (key) => getErrorByField(key, this.errorMessage);

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
    }, {
        validators: [dateRangeValidator('controls.from.value', 'controls.to.value')]
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
      ...!isEmpty(this.URLParams) && this.URLParams,
      from: this.convertDateToFormat(get(this.searchingParametersFormGroup, 'value.from')),
      to: this.convertDateToFormat(get(this.searchingParametersFormGroup, 'value.to')),
      page: pageIndex.toString(),
      size: pageSize.toString()
    };
    formData.status = get(formData, 'trxStatus.status');
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

  convertDateToFormat = (date: moment.Moment | null) => date && date.format('YYYY-MM-DDTHH:mm:ss');

  updateTable() {
    this.dataSource = new MatTableDataSource(this.transactions.content);
  }

  onStepChange(event) {
    if (event.selectedIndex === 1) {
      this.getTransactionList(0, this.pageSize);
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
