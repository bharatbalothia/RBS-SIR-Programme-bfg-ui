import { Component, OnInit, ViewChild, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { FileService } from 'src/app/shared/models/file/file.service';
import { FileCriteriaData } from 'src/app/shared/models/file/file-criteria.model';
import { getFileSearchDisplayName } from '../file-search-display-names';
import { FilesWithPagination } from 'src/app/shared/models/file/files-with-pagination.model';
import { MatTableDataSource } from '@angular/material/table';
import { File } from 'src/app/shared/models/file/file.model';
import { setCalendarDblClick, removeEmpties } from 'src/app/shared/utils/utils';
import { map, startWith, take } from 'rxjs/operators';
import { FILE_DIRECTIONS, getDirectionStringValue } from 'src/app/shared/models/file/file-directions';
import { STATUS_ICON } from 'src/app/core/constants/status-icon';
import { get, isEmpty, isFunction } from 'lodash';
import * as moment from 'moment';
import { TooltipService } from 'src/app/shared/components/tooltip/tooltip.service';
import { ActivatedRoute } from '@angular/router';
import { MatHorizontalStepper } from '@angular/material/stepper';
import { getSearchValidationMessage } from 'src/app/shared/models/search/validation-messages';
import { Observable } from 'rxjs';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { AutorefreshDataComponent } from 'src/app/shared/components/autorefresh-data/autorefresh-data.component';

@Component({
  selector: 'app-file-search',
  templateUrl: './file-search.component.html',
  styleUrls: ['./file-search.component.scss']
})
export class FileSearchComponent implements OnInit, AfterViewInit {

  @ViewChild(AutorefreshDataComponent)
  autoRefreshDataComponent: AutorefreshDataComponent;

  @ViewChild('stepper') stepper: MatHorizontalStepper;

  minDate: moment.Moment = null;
  maxDate: moment.Moment = null;

  searchingParametersFormGroup: FormGroup;
  initialFileCriteriaData: FileCriteriaData;
  fileCriteriaData: FileCriteriaData;
  filteredEntityList: Observable<{ entityName: string, entityId: number }[]>;
  ALL = '';

  setCalendarDblClick = setCalendarDblClick;
  getSearchValidationMessage = getSearchValidationMessage;
  getFileSearchDisplayName = getFileSearchDisplayName;
  FILE_STATUS_ICON = STATUS_ICON;

  isLinear = true;
  isLoading = false;

  defaultSelectedData: { from: moment.Moment, to: moment.Moment } = {
    from: moment().subtract(1, 'days').startOf('day'),
    to: moment().add(1, 'days').endOf('day')
  };

  files: FilesWithPagination;
  dataSource: MatTableDataSource<File>;
  pageIndex = 0;
  pageSize = 100;

  URLParams;

  constructor(
    private formBuilder: FormBuilder,
    private fileService: FileService,
    private toolTip: TooltipService,
    private activatedRoute: ActivatedRoute,
    private cdr: ChangeDetectorRef,
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
      this.initSearchingParametersFormGroup();
      this.initFileCriteriaData();
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
            this.getFileList(0, this.pageSize, () => this.stepper.selectedIndex = idx);
          }
          else {
            this.stepper.selectedIndex = idx;
          }
        };
      });
    });
  }

  isURLParamsEmpty = () => isEmpty(this.URLParams);

  initSearchingParametersFormGroup() {
    this.searchingParametersFormGroup = this.formBuilder.group({
      entityId: [''],
      service: [''],
      direction: [''],
      fileStatus: [''],
      bpstate: [''],
      filename: [''],
      reference: [''],
      type: [''],
      from: [this.defaultSelectedData.from],
      to: [this.defaultSelectedData.to]
    });

    this.initMinMaxDate();
  }

  initMinMaxDate() {
    this.minDate = this.defaultSelectedData.from;
    this.maxDate = this.defaultSelectedData.to;
    this.searchingParametersFormGroup.controls.from.markAsTouched();
    this.searchingParametersFormGroup.controls.to.markAsTouched();
  }

  initFileCriteriaData() {
    this.fileService.getFileCriteriaData()
      .pipe(data => this.setLoading(data))
      .subscribe((data: FileCriteriaData) => {
        this.isLoading = false;
        this.initialFileCriteriaData = this.fileCriteriaData = data;
        this.initFilteredEntityList();
      },
        error => this.isLoading = false
      );
  }

  initFilteredEntityList() {
    this.filteredEntityList = this.searchingParametersFormGroup.controls.entityId.valueChanges
      .pipe(
        startWith(''),
        map(value => this.filterEntityList(value))
      );
  }

  filterFileCriteriaData(): void {
    const service = this.searchingParametersFormGroup.controls.service.value;
    const direction = this.searchingParametersFormGroup.controls.direction.value;
    const outbound = direction === '' ? '' : this.getDirectionValue(direction.toUpperCase());

    this.fileCriteriaData = {
      ...this.initialFileCriteriaData,
      entity: this.initialFileCriteriaData.entity.filter(entity => service === this.ALL || entity.service === service),
      fileStatus: this.initialFileCriteriaData.fileStatus.filter(fileStatus =>
        (service === this.ALL || fileStatus.service === service) &&
        (outbound === this.ALL || fileStatus.outbound === outbound)
      )
    };

    this.initFilteredEntityList();
    this.persistSelectedFileStatus();
  }

  persistSelectedFileStatus() {
    const fileStatusControl = this.searchingParametersFormGroup.controls.fileStatus;
    const initialStatus = fileStatusControl.value;
    const valueInData = this.fileCriteriaData.fileStatus.find((val) => JSON.stringify(val) === JSON.stringify(initialStatus));

    fileStatusControl.setValue(valueInData ? valueInData : '');
  }

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

  getFileList(pageIndex: number, pageSize: number, callback?) {
    const formData = {
      ...!isEmpty(this.URLParams) && this.URLParams,
      ...this.searchingParametersFormGroup.value,
      ...!isEmpty(this.URLParams) && this.URLParams,
      from: this.convertDateToFormat(get(this.searchingParametersFormGroup, 'value.from')),
      to: this.convertDateToFormat(get(this.searchingParametersFormGroup, 'value.to')),
      page: pageIndex.toString(),
      size: pageSize.toString()
    };

    formData.direction = formData.direction && !Array.isArray(formData.direction) ? [formData.direction.toLowerCase()] : formData.direction;
    formData.status = get(formData, 'fileStatus.status');

    if (formData.status) {
      formData.direction = [getDirectionStringValue(get(formData, 'fileStatus.outbound')).toLowerCase()];
      formData.service = get(formData, 'fileStatus.service');
    }

    formData.fileStatus = null;

    this.isLoading = true;
    this.fileService.getFileList(removeEmpties(formData)).pipe(take(1)).subscribe((data: FilesWithPagination) => {
      this.isLoading = false;
      this.pageIndex = pageIndex;
      this.pageSize = pageSize;
      this.files = data;
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
    this.dataSource = new MatTableDataSource(this.files.content);
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
    this.initSearchingParametersFormGroup();
    this.initFileCriteriaData();
  }

  getDirectionValue = (direction) => direction === FILE_DIRECTIONS.OUTBOUND;

  getTooltip(step: string, field: string): string {
    const toolTip = this.toolTip.getTooltip({
      type: 'file-search',
      qualifier: step,
      mode: 'search',
      fieldName: field
    });
    return toolTip.length > 0 ? toolTip : this.getFileSearchDisplayName(field);
  }

  handleDate = (event: any, field: string) => this[field] = event.value;

  onNext = (shouldDisableNext?: boolean) => {
    if (this.isValidEntity()) {
      this.getFileList(0, this.pageSize, !shouldDisableNext && (() => this.stepper.next()));
    }
  }

  onEntitySelect = (event?: MatAutocompleteSelectedEvent) => {
    const entityId = this.searchingParametersFormGroup.controls.entityId.value;
    const service = this.searchingParametersFormGroup.controls.service.value;
    const entityService = entityId === this.ALL ?
      this.ALL :
      this.fileCriteriaData.entity.find(entity => entity.entityId === entityId).service;

    // Filter file status when entity service is not equal to service
    if (service !== entityService) {
      this.searchingParametersFormGroup.controls.service.setValue(entityService);
      this.filterFileCriteriaData();
    }
  }

  onServiceSelect = (event) => {
    this.searchingParametersFormGroup.controls.entityId.setValue(this.ALL);
    this.filterFileCriteriaData();
  }

  onDirectionSelect = (event) => {
    this.filterFileCriteriaData();
  }

  onStatusSelect = (event) => {
    // const fromStatus = event.value;

    // if (fromStatus !== this.ALL) {
    //   let refreshRequired = false;
    //   const serviceControl = this.searchingParametersFormGroup.controls.service;
    //   const initialService = serviceControl.value;
    //   const directionControl = this.searchingParametersFormGroup.controls.direction;
    //   const initialDirection = directionControl.value;

    //   const newService = this.fileCriteriaData.service.find((element) =>
    //     element.toUpperCase() === fromStatus.service.toUpperCase()
    //   );

    //   const newDirection = this.fileCriteriaData.direction.find((element) =>
    //     element.toUpperCase() === getDirectionStringValue(fromStatus.outbound)
    //   );

    //   if (newDirection && (initialDirection !== newDirection)) {
    //     directionControl.setValue(newDirection);
    //     refreshRequired = true;
    //   }

    //   if (newService && (initialService !== newService)) {
    //     serviceControl.setValue(newService);
    //     refreshRequired = true;
    //   }

    //   if (refreshRequired) {
    //     this.filterFileCriteriaData();
    //   }
    // }
  }

  isValidEntity(): boolean {
    const entityId = this.searchingParametersFormGroup.controls.entityId.value;

    if (entityId === this.ALL) {
      return true;
    }

    return !!this.fileCriteriaData.entity.find(entity => entity.entityId === entityId);
  }

  displayEntity(value?: number) {
    if (value === null) {
      this.searchingParametersFormGroup.controls.entityId.setValue(this.ALL);
      this.onEntitySelect();
    }

    return value ? this.fileCriteriaData.entity.find(entity => entity.entityId === value).entityName : 'ALL';
  }

  private filterEntityList(value: string | number) {
    if (typeof value === 'string') {
      return value ?
        this.fileCriteriaData.entity.filter(option => option.entityName.toLowerCase().includes(value.toLowerCase())) :
        this.fileCriteriaData.entity;
    } else {
      return value ?
        this.fileCriteriaData.entity.filter(option => option.entityId === value) : this.fileCriteriaData.entity;
    }
  }
}
