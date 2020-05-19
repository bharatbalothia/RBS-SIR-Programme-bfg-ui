import { Component, Input, OnChanges, SimpleChanges, Output, EventEmitter } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-progress-bar',
  templateUrl: './progress-bar.component.html',
  styleUrls: ['./progress-bar.component.scss']
})
export class ProgressBarComponent implements OnChanges {
  isLoading: boolean;
  @Output() stateChange = new EventEmitter<boolean>(true);  // isAsync = true to prevent ExpressionChangedAfterItHasBeenCheckedError
  @Input() loading: Observable<any>;
  @Output() loadingError = new EventEmitter<any>();

  constructor() { }

  setLoading(value: boolean) {
    this.isLoading = value;
    this.stateChange.emit(value);
  }

  ngOnChanges(changes: SimpleChanges): void {
    const loadingChange = changes.loading;
    if (loadingChange) {
      if (loadingChange.currentValue) {
        this.setLoading(true);
        loadingChange.currentValue.subscribe(() => this.onSuccess(), (error) => this.onError(error));
      } else {
        this.setLoading(false);
      }
    }
  }

  private onSuccess() {
    this.setLoading(false);
  }

  private onError(error: any) {
    this.setLoading(false);
    this.loadingError.emit(error);
  }

}
