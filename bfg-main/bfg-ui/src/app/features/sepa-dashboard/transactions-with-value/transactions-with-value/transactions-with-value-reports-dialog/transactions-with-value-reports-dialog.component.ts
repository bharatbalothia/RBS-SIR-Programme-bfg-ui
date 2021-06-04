import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DetailsDialogData } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { getSearchValidationMessage } from 'src/app/shared/models/search/validation-messages';
import { SEPADashboardService } from 'src/app/shared/models/sepa-dashboard/sepa-dashboard.service';
import { removeEmpties, setCalendarDblClick } from 'src/app/shared/utils/utils';
import { getTransactionsWithValueDisplayName } from '../../transactions-with-value-display-names';
import { saveAs } from 'file-saver';
import { get } from 'lodash';
import * as moment from 'moment';
import { REPORT_TYPE } from 'src/app/shared/constants/report-types';

@Component({
  selector: 'app-transactions-with-value-reports-dialog',
  templateUrl: './transactions-with-value-reports-dialog.component.html',
  styleUrls: ['./transactions-with-value-reports-dialog.component.scss']
})
export class TransactionsWithValueReportsDialogComponent implements OnInit {

  REPORT_TYPE = REPORT_TYPE;
  setCalendarDblClick = setCalendarDblClick;
  getSearchValidationMessage = getSearchValidationMessage;
  getTransactionsWithValueDisplayName = getTransactionsWithValueDisplayName;

  isLoading = false;

  minDate: moment.Moment = null;
  maxDate: moment.Moment = null;
  reportsParametersFormGroup: FormGroup;

  defaultSelectedData: { from: moment.Moment, to: moment.Moment } = {
    from: moment().startOf('day'),
    to: moment().endOf('day')
  };

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private formBuilder: FormBuilder,
    private SEPADashboardService: SEPADashboardService
  ) {
    this.data.yesCaption = this.data.yesCaption || 'Close';
  }

  ngOnInit(): void {
    this.initSearchingParametersFormGroup();
  }

  initSearchingParametersFormGroup() {
    this.reportsParametersFormGroup = this.formBuilder.group({
      from: [this.defaultSelectedData.from],
      to: [this.defaultSelectedData.to]
    });

    this.initMinMaxDate();
  }

  initMinMaxDate() {
    this.minDate = this.defaultSelectedData.from;
    this.maxDate = this.defaultSelectedData.to;
    this.reportsParametersFormGroup.controls.from.markAsTouched();
    this.reportsParametersFormGroup.controls.to.markAsTouched();
  }

  convertDateToFormat = (date: moment.Moment | null) => date && date.format('YYYY-MM-DDTHH:mm:ss');

  handleDate = (event: any, field: string) => this[field] = event.value;

  resetControl = (control: AbstractControl, field?: string) => {
    control.reset();
    if (field) {
      this[field] = '';
    }
  }

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

  exportReport = (type: string) => this.SEPADashboardService.exportReport(removeEmpties({
    from: this.convertDateToFormat(get(this.reportsParametersFormGroup, 'value.from')),
    to: this.convertDateToFormat(get(this.reportsParametersFormGroup, 'value.to')),
    type
  })).pipe(data => this.setLoading(data)).toPromise().then((response: any) => saveAs(response.body, response.headers.get('content-disposition').split(';')[1].trim().split('=')[1]))
    .finally(() => this.isLoading = false)

}
