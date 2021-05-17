import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';
import 'moment-timezone';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { SEPATraffic } from 'src/app/shared/models/statistics/sepa-traffic.model';
import { StatisticsService } from 'src/app/shared/models/statistics/statistics.service';
import { NotificationService } from 'src/app/shared/services/notification.service';
import { getMappedObjectArray } from 'src/app/shared/utils/utils';

@Component({
  selector: 'app-list-of-files',
  templateUrl: './list-of-files.component.html',
  styleUrls: ['./list-of-files.component.scss']
})
export class ListOfFilesComponent implements OnInit {

  ROUTING_PATHS = ROUTING_PATHS;

  isLoading = false;

  updateTime: string;
  SEPATraffic: { key: string, value: string }[] = [];

  constructor(
    private statisticsService: StatisticsService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.getSEPATraffic();
  }

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

  getSEPATraffic = () =>
    this.statisticsService.getSEPATraffic().pipe(data => this.setLoading(data)).subscribe((data: SEPATraffic) => {
      this.isLoading = false;
      this.SEPATraffic = getMappedObjectArray(data);
      this.updateTime = moment().tz('Europe/London').format('DD/MM/YYYY hh:mm:ss');
    }, () => this.isLoading = false)

  checkCount = (count: number) => count === 0 &&
    this.notificationService.show('File search', 'No files matched your search criteria', 'warning')
}
