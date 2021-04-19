import { Component, OnInit } from '@angular/core';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { Alerts } from 'src/app/shared/models/statistics/alerts.model';
import { StatisticsService } from 'src/app/shared/models/statistics/statistics.service';
import { SystemErrors } from 'src/app/shared/models/statistics/system-errors.model';
import * as moment from 'moment';
import 'moment-timezone';

@Component({
  selector: 'app-home-alerts',
  templateUrl: './home-alerts.component.html',
  styleUrls: ['./home-alerts.component.scss']
})
export class HomeAlertsComponent implements OnInit {

  ROUTING_PATHS = ROUTING_PATHS;

  isLoading = false;

  systemErrors: SystemErrors;
  alerts: Alerts;
  updateTime: string;

  constructor(
    private statisticsService: StatisticsService,
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
    this.alerts = data;
    this.isLoading = false;
  },
    error => this.isLoading = false,
    () => this.updateTime = moment().tz('Europe/London').format('DD/MM/YYYY hh:mm:ss')
  )

  getMinusMonthsDate = (months) => moment().tz('Europe/London').subtract(months, 'months').format('YYYY-MM-DDTHH:mm:ss');

}
