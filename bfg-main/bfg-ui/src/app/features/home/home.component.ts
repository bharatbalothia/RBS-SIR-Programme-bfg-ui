import { Component, OnInit } from '@angular/core';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { ErrorMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { Alerts } from 'src/app/shared/models/statistics/alerts.model';
import { SCTTraffic } from 'src/app/shared/models/statistics/sct-traffic.model';
import { StatisticsService } from 'src/app/shared/models/statistics/statistics.service';
import { SystemErrors } from 'src/app/shared/models/statistics/system-errors.model';
import * as moment from 'moment';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  ROUTING_PATHS = ROUTING_PATHS;

  errorMessage: ErrorMessage;
  isLoading = false;

  systemErrors: SystemErrors;
  alerts: Alerts;
  SCTTraffic: SCTTraffic;

  constructor(
    private statisticsService: StatisticsService
  ) { }

  ngOnInit(): void {
    this.getSystemErrors();
  }

  setLoading(data) {
    this.errorMessage = null;
    this.isLoading = true;
    return data;
  }

  getSystemErrors = () =>
    this.statisticsService.getSystemErrors().pipe(data => this.setLoading(data)).subscribe((data: SystemErrors) => {
      this.getAlerts();
      this.systemErrors = data;
    },
      error => {
        this.isLoading = false;
        this.errorMessage = getApiErrorMessage(error);
      })

  getAlerts = () => this.statisticsService.getAlerts().pipe(data => this.setLoading(data)).subscribe((data: Alerts) => {
    this.getSCTTraffic();
    this.alerts = data;
  },
    error => {
      this.isLoading = false;
      this.errorMessage = getApiErrorMessage(error);
    })

  getSCTTraffic = () => this.statisticsService.getSCTTraffic().pipe(data => this.setLoading(data)).subscribe((data: SCTTraffic) => {
    this.isLoading = false;
    this.SCTTraffic = data;
  },
    error => {
      this.isLoading = false;
      this.errorMessage = getApiErrorMessage(error);
    })

  getMinusMonthsDate = (months) => moment().utcOffset(0).subtract(months, 'months').format('YYYY-MM-DDTHH:mm:ss');

  getMinusHoursDate = (hours) => moment().utcOffset(0).subtract(hours, 'hours').format('YYYY-MM-DDTHH:mm:ss');

  getMinusDaysDate = (days) => moment().utcOffset(0).subtract(days, 'days').format('YYYY-MM-DDTHH:mm:ss');

  getCurrentDate = () => moment().utcOffset(0).format('YYYY-MM-DDTHH:mm:ss');
}
