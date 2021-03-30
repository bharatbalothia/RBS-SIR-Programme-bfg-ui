import { Component, OnInit } from '@angular/core';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { Alerts } from 'src/app/shared/models/statistics/alerts.model';
import { SCTTraffic } from 'src/app/shared/models/statistics/sct-traffic.model';
import { StatisticsService } from 'src/app/shared/models/statistics/statistics.service';
import { SystemErrors } from 'src/app/shared/models/statistics/system-errors.model';
import * as moment from 'moment';
import { NotificationService } from 'src/app/shared/services/notification.service';
import 'moment-timezone';
import { getDirectionIcon } from '../search/transaction-search/transaction-search-display-names';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  getDirectionIcon = getDirectionIcon;
  ROUTING_PATHS = ROUTING_PATHS;

  isLoading = false;

  systemErrors: SystemErrors;
  alerts: Alerts;
  SCTTraffic: SCTTraffic;
  updateTime: string;

  constructor(
    private statisticsService: StatisticsService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.getSystemErrors();
  }

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

  getSystemErrors = () =>
    this.statisticsService.getSystemErrors().pipe(data => this.setLoading(data)).subscribe((data: SystemErrors) => {
      this.getAlerts();
      this.systemErrors = data;
    },
      error => this.isLoading = false
    )

  getAlerts = () => this.statisticsService.getAlerts().pipe(data => this.setLoading(data)).subscribe((data: Alerts) => {
    this.getSCTTraffic();
    this.alerts = data;
  },
    error => this.isLoading = false
  )

  getSCTTraffic = () => this.statisticsService.getSCTTraffic().pipe(data => this.setLoading(data))
    .subscribe(
      (data: SCTTraffic) => {
        this.isLoading = false;
        this.SCTTraffic = data;
      },
      error => this.isLoading = false,
      ()  => this.updateTime = moment().tz('Europe/London').format('DD/MM/YYYY hh:mm:ss')
    )

  checkCount = (count: number, rote: string) => count === 0 &&
    this.notificationService.show(
      rote === ROUTING_PATHS.FILE_SEARCH ? 'File search' : 'SCT transaction search',
      rote === ROUTING_PATHS.FILE_SEARCH ? 'No files matched your search criteria' : 'No transactions matched your search criteria',
      'warning'
    )

  getMinusMonthsDate = (months) => moment().utcOffset(0).subtract(months, 'months').format('YYYY-MM-DDTHH:mm:ss');

  getMinusHoursDate = (hours) => moment().utcOffset(0).subtract(hours, 'hours').format('YYYY-MM-DDTHH:mm:ss');

  getMinusDaysDate = (days) => moment().utcOffset(0).subtract(days, 'days').format('YYYY-MM-DDTHH:mm:ss');

  getCurrentDate = () => moment().utcOffset(0).format('YYYY-MM-DDTHH:mm:ss');
}
