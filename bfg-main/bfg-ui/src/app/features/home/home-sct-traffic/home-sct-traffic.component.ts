import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';
import 'moment-timezone';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { SCTTraffic } from 'src/app/shared/models/statistics/sct-traffic.model';
import { StatisticsService } from 'src/app/shared/models/statistics/statistics.service';
import { NotificationService } from 'src/app/shared/services/notification.service';
import { getDirectionIcon } from '../../search/transaction-search/transaction-search-display-names';

@Component({
  selector: 'app-home-sct-traffic',
  templateUrl: './home-sct-traffic.component.html',
  styleUrls: ['./home-sct-traffic.component.scss']
})
export class HomeSCTTrafficComponent implements OnInit {

  getDirectionIcon = getDirectionIcon;
  ROUTING_PATHS = ROUTING_PATHS;

  isLoading = false;

  SCTTraffic: SCTTraffic;
  updateTime: string;

  constructor(
    private statisticsService: StatisticsService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.getSCTTraffic();
  }

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

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
