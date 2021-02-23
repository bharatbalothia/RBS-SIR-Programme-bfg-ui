import { AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { get, isEmpty, isFunction } from 'lodash';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { map, startWith, take } from 'rxjs/operators';
import { TransactionsWithPagination } from 'src/app/shared/models/transaction/transactions-with-pagination.model';
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
import { getSearchValidationMessage } from 'src/app/shared/models/search/validation-messages';
import { ENTITY_SERVICE_TYPE } from 'src/app/shared/models/entity/entity-constants';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-transaction-search',
  templateUrl: './transaction-search.component.html',
  styleUrls: ['./transaction-search.component.scss']
})
export class TransactionSearchComponent implements OnInit, AfterViewInit {

  @ViewChild(TransactionTableComponent)
  transactionTableComponent: TransactionTableComponent;

  @ViewChild('stepper') stepper: MatHorizontalStepper;

  minDate: moment.Moment = null;
  maxDate: moment.Moment = null;

  searchingParametersFormGroup: FormGroup;
  transactionCriteriaData: TransactionCriteriaData;
  filteredEntityList: Observable<string[]>;

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
      this.onNext();
      this.cdr.detectChanges();
    }
    setTimeout(() => {
      this.stepper.steps.forEach((step, idx) => {
        step.select = () => {
          if (idx === 1 && this.searchingParametersFormGroup.valid) {
            this.getTransactionList(0, this.pageSize, () => this.stepper.selectedIndex = idx);
          }
          else {
            this.stepper.selectedIndex = idx;
          }
        };
      });
    });
  }

  isURLParamsEmpty = () => isEmpty(this.URLParams);

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
    this.initMinMaxDate();
  }

  initMinMaxDate() {
    this.minDate = this.defaultSelectedData.from;
    this.maxDate = this.defaultSelectedData.to;
    this.searchingParametersFormGroup.controls.from.markAsTouched();
    this.searchingParametersFormGroup.controls.to.markAsTouched();
  }

  getTransactionCriteriaData = () =>
    this.transactionService.getTransactionCriteriaData(removeEmpties(this.criteriaFilterObject))
      .pipe(data => this.setLoading(data))
      .subscribe((data: TransactionCriteriaData) => {
        this.isLoading = false;
        this.transactionCriteriaData = {
          ...data,
          entity: [ 'ALL', ...data.entity ]
        };
        this.filteredEntityList = this.searchingParametersFormGroup.controls.entity.valueChanges
          .pipe(
            startWith(''),
            map(value => this._filterEntityList(value))
          );
        this.persistSelectedTransactionStatus();
      },
        error => this.isLoading = false
      )

  displayEntity(value?: string) {
    return value ? this.transactionCriteriaData.entity.find(entity => entity === value) : 'ALL';
  }

  private _filterEntityList(value: string) {
    return value ?
      this.transactionCriteriaData.entity.filter(option => option.toLowerCase().includes(value.toLowerCase())) :
      this.transactionCriteriaData.entity;
  }

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
    this.isLoading = true;
    return data;
  }

  getTransactionList(pageIndex: number, pageSize: number, callback?) {
    const formData = {
      ...this.searchingParametersFormGroup.value,
      ...!isEmpty(this.URLParams) && this.URLParams,
      from: this.convertDateToFormat(get(this.searchingParametersFormGroup, 'value.from')),
      to: this.convertDateToFormat(get(this.searchingParametersFormGroup, 'value.to')),
      page: pageIndex.toString(),
      size: pageSize.toString(),
      service: ENTITY_SERVICE_TYPE.SCT
    };

    // Don't send 'All' to backend.
    if (formData.entity === 'ALL') {
      delete formData.entity;
    }

    formData.direction = formData.direction && !Array.isArray(formData.direction) ? [formData.direction.toLowerCase()] : formData.direction;
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
        if (isFunction(callback)) {
          callback();
        }
      },
        error => this.isLoading = false
      );
  }

  convertDateToFormat = (date: moment.Moment | null) => date && date.format('YYYY-MM-DDTHH:mm:ss');

  updateTable() {
    this.dataSource = new MatTableDataSource(this.transactions.content);
  }

  onStepChange(event) {
    if (event.selectedIndex === 1) {
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

  handleDate(event: any, field: string) {
    const date: moment.Moment = event.value;

    if (date) {
      this[field] = date;
    }
  }

  onNext = () => this.getTransactionList(0, this.pageSize, () => this.stepper.next());

}
