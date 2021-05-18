import { AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { get, isEmpty, isFunction } from 'lodash';
import { removeEmpties, setCalendarDblClick } from 'src/app/shared/utils/utils';
import { map, startWith, take } from 'rxjs/operators';
import { TransactionsWithPagination } from 'src/app/shared/models/transaction/transactions-with-pagination.model';
import * as moment from 'moment';
import { FormGroup, FormBuilder } from '@angular/forms';
import { TransactionCriteriaData } from 'src/app/shared/models/transaction/transaction-criteria.model';
import { getTransactionSearchDisplayName } from '../transaction-search-display-names';
import { MatTableDataSource } from '@angular/material/table';
import { Transaction } from 'src/app/shared/models/transaction/transaction.model';
import { TransactionService } from 'src/app/shared/models/transaction/transaction.service';
import { FILE_DIRECTIONS, getDirectionStringValue } from 'src/app/shared/models/file/file-directions';
import { ActivatedRoute } from '@angular/router';
import { TooltipService } from 'src/app/shared/components/tooltip/tooltip.service';
import { MatHorizontalStepper } from '@angular/material/stepper';
import { getSearchValidationMessage } from 'src/app/shared/models/search/validation-messages';
import { ENTITY_SERVICE_TYPE } from 'src/app/shared/models/entity/entity-constants';
import { Observable } from 'rxjs';
import { AutorefreshDataComponent } from 'src/app/shared/components/autorefresh-data/autorefresh-data.component';

@Component({
  selector: 'app-transaction-search',
  templateUrl: './transaction-search.component.html',
  styleUrls: ['./transaction-search.component.scss']
})
export class TransactionSearchComponent implements OnInit, AfterViewInit {

  @ViewChild(AutorefreshDataComponent)
  autoRefreshDataComponent: AutorefreshDataComponent;

  @ViewChild('stepper') stepper: MatHorizontalStepper;

  minDate: moment.Moment = null;
  maxDate: moment.Moment = null;
  pMinDate: moment.Moment = null;
  pMaxDate: moment.Moment = null;

  searchingParametersFormGroup: FormGroup;
  initialTransactionCriteriaData: TransactionCriteriaData;
  transactionCriteriaData: TransactionCriteriaData;
  filteredEntityList: Observable<string[]>;
  ALL = '';

  getSearchValidationMessage = getSearchValidationMessage;
  getTransactionSearchDisplayName = getTransactionSearchDisplayName;
  setCalendarDblClick = setCalendarDblClick;
  isLinear = true;
  isLoading = false;

  defaultSelectedData: { pFrom: moment.Moment, pTo: moment.Moment, from: moment.Moment, to: moment.Moment } = {
    pFrom: null,
    pTo: null,
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
      if (params.from) {
        this.defaultSelectedData = {
          pFrom: params.from === 'none' ? null : moment(params.from),
          pTo: null,
          to: null,
          from: null
        };
        delete this.URLParams.from;
      }
      this.initializeSearchingParametersFormGroup();
      this.initTransactionCriteriaData();
    });
  }

  ngAfterViewInit() {
    if (!this.isURLParamsEmpty()) {
      this.stepper.selectedIndex = 1;
      this.onNext(true);
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
      pFrom: [this.defaultSelectedData.pFrom],
      pTo: [this.defaultSelectedData.pTo],
      from: [this.defaultSelectedData.from],
      to: [this.defaultSelectedData.to]
    });

    this.criteriaFilterObject = { direction: '', trxStatus: '' };
    this.initMinMaxDate();
  }

  initMinMaxDate() {
    this.pMinDate = this.defaultSelectedData.pFrom;
    this.pMaxDate = this.defaultSelectedData.pTo;
    this.minDate = this.defaultSelectedData.from;
    this.maxDate = this.defaultSelectedData.to;
    this.searchingParametersFormGroup.controls.pFrom.markAsTouched();
    this.searchingParametersFormGroup.controls.pTo.markAsTouched();
    this.searchingParametersFormGroup.controls.from.markAsTouched();
    this.searchingParametersFormGroup.controls.to.markAsTouched();
  }

  initTransactionCriteriaData = () =>
    this.transactionService.getTransactionCriteriaData(removeEmpties(this.criteriaFilterObject))
      .pipe(data => this.setLoading(data))
      .subscribe((data: TransactionCriteriaData) => {
        this.isLoading = false;
        const direction = get(this.transactionCriteriaData, 'direction');
        if (direction) {
          this.initialTransactionCriteriaData = this.transactionCriteriaData = {
            ...data,
            direction
          };
        }
        else {
          this.initialTransactionCriteriaData = this.transactionCriteriaData = data;
        }
        this.initFilteredEntityList();
      },
        error => this.isLoading = false
      )

  initFilteredEntityList() {
    this.filteredEntityList = this.searchingParametersFormGroup.controls.entity.valueChanges
      .pipe(
        startWith(''),
        map(value => this._filterEntityList(value))
      );
  }

  filterTransactionCriteriaData(): void {
    const service = this.searchingParametersFormGroup.controls.service.value;
    const direction = get(this.searchingParametersFormGroup.controls.direction.value, 'direction', '');
    const outbound = direction === '' ? '' : this.getDirectionValue(direction.toUpperCase());

    this.transactionCriteriaData = {
      ...this.initialTransactionCriteriaData,
      trxStatus: this.initialTransactionCriteriaData.trxStatus.filter(fileStatus =>
        (service === this.ALL || fileStatus.service === service) &&
        (outbound === this.ALL || fileStatus.outbound === outbound)
      )
    };

    this.initFilteredEntityList();
    this.persistSelectedTransactionStatus();
  }

  getDirectionValue = (direction) => direction === FILE_DIRECTIONS.OUTBOUND;

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
      pFrom: this.convertDateToFormat(get(this.searchingParametersFormGroup, 'value.pFrom')),
      pTo: this.convertDateToFormat(get(this.searchingParametersFormGroup, 'value.pTo')),
      from: this.convertDateToFormat(get(this.searchingParametersFormGroup, 'value.from')),
      to: this.convertDateToFormat(get(this.searchingParametersFormGroup, 'value.to')),
      page: pageIndex.toString(),
      size: pageSize.toString(),
      service: ENTITY_SERVICE_TYPE.SCT
    };

    if (get(formData, 'direction.payaway')) {
      formData.payaway = formData.direction.payaway;
    }
    formData.direction = formData.direction && !Array.isArray(formData.direction)
      ? [formData.direction.direction.toLowerCase()] : formData.direction;
    formData.status = get(formData, 'trxStatus.status');

    if (formData.status && !formData.direction) {
      formData.direction = [getDirectionStringValue(get(formData, 'trxStatus.outbound')).toLowerCase()];
      formData.service = get(formData, 'trxStatus.service');
    }

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
      this.autoRefreshDataComponent.autoRefreshChange(true);
    }
    else {
      this.autoRefreshDataComponent.autoRefreshChange(false);
    }
  }

  resetSearchParameters = () => {
    this.initializeSearchingParametersFormGroup();
    this.initTransactionCriteriaData();
  }

  onDirectionSelect = (event) => {
    this.criteriaFilterObject.direction = event.value.direction;
    this.filterTransactionCriteriaData();
  }

  onStatusSelect = (event) => {
    this.setDirectionFromStatus(event.value);
  }

  setDirectionFromStatus(fromStatus) {
    if (fromStatus !== '') {
      let refreshRequired = false;
      const directionControl = this.searchingParametersFormGroup.controls.direction;
      const initialDirection = get(directionControl.value, 'direction');

      const newDirection = this.transactionCriteriaData.direction
        .find((element) => element.direction.toUpperCase() === getDirectionStringValue(fromStatus.outbound));

      if (newDirection && (initialDirection !== get(newDirection, 'direction'))) {
        directionControl.setValue(newDirection);
        refreshRequired = true;
        this.criteriaFilterObject.direction = get(newDirection, 'direction');
      }
      if (refreshRequired) {
        this.filterTransactionCriteriaData();
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

  handleDate = (event: any, field: string) => this[field] = event.value;

  isValidEntity(): boolean {
    const entity = this.searchingParametersFormGroup.controls.entity.value;

    if (entity === this.ALL) {
      return true;
    }

    return !!this.transactionCriteriaData.entity.find(value => value === entity);
  }

  onNext = (shouldDisableNext?: boolean) => {
    if (this.isValidEntity()) {
      this.getTransactionList(0, this.pageSize, !shouldDisableNext && (() => this.stepper.next()));
    }
  }

  displayEntity(value?) {
    if (value === null) {
      this.searchingParametersFormGroup.controls.entity.setValue(this.ALL);
    }

    return value ? this.transactionCriteriaData.entity.find(entity => entity === value) : 'ALL';
  }

  private _filterEntityList(value: string) {
    return value ?
      this.transactionCriteriaData.entity.filter(option => option.toLowerCase().includes(value.toLowerCase())) :
      this.transactionCriteriaData.entity;
  }
}
