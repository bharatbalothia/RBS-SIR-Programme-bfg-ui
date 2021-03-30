import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { interval, Subscription } from 'rxjs';

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

  constructor() { }

  ngOnInit(): void {
    this.autoRefreshChange(this.shouldAutoRefresh);
  }

  autoRefreshChange = (value) => {
    if (value) {
      this.autoRefreshing = interval(this.refreshInterval).subscribe(() => this.getData());
    }
    else if (this.autoRefreshing) {
      this.autoRefreshing.unsubscribe();
    }
  }

  ngOnDestroy(): void {
    this.autoRefreshChange(false);
  }

}
