import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { interval, Subscription } from 'rxjs';
import { AutoRefreshService } from '../../services/autorefresh.service';

@Component({
  selector: 'app-autorefresh-data',
  templateUrl: './autorefresh-data.component.html',
  styleUrls: ['./autorefresh-data.component.scss']
})
export class AutorefreshDataComponent implements OnInit, OnDestroy {

  @Input() shouldAutoRefresh = true;
  @Input() getData: () => any;

  refreshInterval = 60000;

  autoRefreshing: Subscription;

  constructor(private autoRefreshService: AutoRefreshService) { }

  ngOnInit(): void {
    this.autoRefreshChange(this.shouldAutoRefresh);
  }

  autoRefreshChange = (value) => {
    if (value) {
      this.autoRefreshing = interval(this.refreshInterval).subscribe(() => {
        this.getData();
        this.autoRefreshService.setAutoRefresh(true);
      });
    }
    else if (this.autoRefreshing) {
      this.autoRefreshService.setAutoRefresh(false);
      this.autoRefreshing.unsubscribe();
    }
  }

  ngOnDestroy(): void {
    this.autoRefreshChange(false);
  }

}
