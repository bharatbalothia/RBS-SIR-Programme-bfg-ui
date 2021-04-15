import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';
import 'moment-timezone';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { Alerts } from 'src/app/shared/models/statistics/alerts.model';
import { SCTTraffic } from 'src/app/shared/models/statistics/sct-traffic.model';
import { StatisticsService } from 'src/app/shared/models/statistics/statistics.service';
import { SystemErrors } from 'src/app/shared/models/statistics/system-errors.model';
import { NotificationService } from 'src/app/shared/services/notification.service';
import { getDirectionIcon } from '../../search/transaction-search/transaction-search-display-names';

@Component({
  selector: 'app-home-statistics',
  templateUrl: './home-statistics.component.html',
  styleUrls: ['./home-statistics.component.scss']
})
export class HomeStatisticsComponent implements OnInit {

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
      () => this.updateTime = moment().tz('Europe/London').format('DD/MM/YYYY hh:mm:ss')
    )

  checkCount = (count: number, rote: string) => count === 0 &&
    this.notificationService.show(
      rote === ROUTING_PATHS.FILE_SEARCH ? 'File search' : 'SCT transaction search',
      rote === ROUTING_PATHS.FILE_SEARCH ? 'No files matched your search criteria' : 'No transactions matched your search criteria',
      'warning'
    )

  getMinusMonthsDate = (months) => moment().tz('Europe/London').subtract(months, 'months').format('YYYY-MM-DDTHH:mm:ss');

  getMinusHoursDate = (hours) => moment().tz('Europe/London').subtract(hours, 'hours').format('YYYY-MM-DDTHH:mm:ss');

  getMinusDaysDate = (days) => moment().tz('Europe/London').subtract(days, 'days').format('YYYY-MM-DDTHH:mm:ss');

  getCurrentDate = () => moment().tz('Europe/London').format('YYYY-MM-DDTHH:mm:ss');

}
