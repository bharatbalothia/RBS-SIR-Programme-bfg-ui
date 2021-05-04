import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { getDirectionIcon } from 'src/app/features/search/transaction-search/transaction-search-display-names';
import { SEPAFile } from 'src/app/shared/models/sepa-dashboard/sepa-file.model';
import { SEPAFilesWithPagination } from 'src/app/shared/models/sepa-dashboard/sepa-files-with-pagination.model';
import { SEPADashboardService } from 'src/app/shared/models/sepa-dashboard/sepa-dashboard.service';
import { take } from 'rxjs/operators';
import { FileDialogService } from 'src/app/shared/models/file/file-dialog.service';
import { removeEmpties, setCalendarDblClick } from 'src/app/shared/utils/utils';
import { getSearchValidationMessage } from 'src/app/shared/models/search/validation-messages';
import { getFileSearchDisplayName } from 'src/app/features/search/file-search/file-search-display-names';
import { AbstractControl, FormBuilder, FormGroup } from '@angular/forms';
import * as moment from 'moment';
import { get } from 'lodash';

@Component({
  selector: 'app-transactions-with-value',
  templateUrl: './transactions-with-value.component.html',
  styleUrls: ['./transactions-with-value.component.scss']
})
export class TransactionsWithValueComponent implements OnInit {

  getDirectionIcon = getDirectionIcon;
  setCalendarDblClick = setCalendarDblClick;
  getSearchValidationMessage = getSearchValidationMessage;
  getFileSearchDisplayName = getFileSearchDisplayName;

  isLoading: boolean;

  shouldHidePaginator = false;

  minDate: moment.Moment = null;
  maxDate: moment.Moment = null;

  searchingParametersFormGroup: FormGroup;

  SEPAFiles: SEPAFilesWithPagination;

  dataSource: MatTableDataSource<SEPAFile>;
  displayedColumns: string[] = [
    'filename',
    'type',
    'transactions',
    'totalSettleAmount'
  ];

  pageIndex = 0;
  pageSize = 10;
  pageSizeOptions: number[] = [5, 10, 20, 50, 100];

  defaultSelectedData: { from: moment.Moment, to: moment.Moment } = {
    from: null,
    to: null
  };

  constructor(
    private SEPADashboardService: SEPADashboardService,
    private fileDialogService: FileDialogService,
    private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.initSearchingParametersFormGroup();
    this.getSEPAFileList(this.pageIndex, this.pageSize);
    this.fileDialogService.isLoadingChange.subscribe(data => this.isLoading = data);
  }

  initSearchingParametersFormGroup() {
    this.searchingParametersFormGroup = this.formBuilder.group({
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

  convertDateToFormat = (date: moment.Moment | null) => date && date.format('YYYY-MM-DDTHH:mm:ss');

  handleDate = (event: any, field: string) => this[field] = event.value;

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

  getSEPAFileList(pageIndex: number, pageSize: number) {
    const formData = {
      from: this.convertDateToFormat(get(this.searchingParametersFormGroup, 'value.from')),
      to: this.convertDateToFormat(get(this.searchingParametersFormGroup, 'value.to')),
      page: pageIndex.toString(),
      size: pageSize.toString()
    };

    this.SEPADashboardService.getSEPAFileList(removeEmpties(formData))
      .pipe(data => this.setLoading(data))
      .pipe(take(1)).subscribe((data: SEPAFilesWithPagination) => {
        this.isLoading = false;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.SEPAFiles = data;
        this.updateTable();
      },
        error => this.isLoading = false
      );
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.SEPAFiles.content);
  }

  getSearchingTableHeader(totalElements: number, pageSize: number, page: number) {
    const start = (page * pageSize) - (pageSize - 1);
    const end = Math.min(start + pageSize - 1, totalElements);
    return `Items ${start}-${end} of ${totalElements}`;
  }

  openFileDocumentInfo = (file) => this.fileDialogService.openFileDocumentInfo(file, true);

  openTransactionsDialog = (file) => this.fileDialogService.openTransactionsDialog(file);

  resetControl = (control: AbstractControl, field?: string) => {
    control.reset();
    if (field) {
      this[field] = '';
    }
    this.getSEPAFileList(this.pageIndex, this.pageSize);
  }

}
