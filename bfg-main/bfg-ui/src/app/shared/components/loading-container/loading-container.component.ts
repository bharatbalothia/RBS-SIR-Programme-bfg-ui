import { Component, OnInit, OnChanges, Input, Output, EventEmitter, SimpleChanges } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-loading-container',
  templateUrl: './loading-container.component.html',
  styleUrls: ['./loading-container.component.scss']
})
export class LoadingContainerComponent implements OnInit, OnChanges {
  error: any;
  isLoading: boolean;

  @Input() loading: Observable<any>;
  @Output() stateChange = new EventEmitter<boolean>(true);  // isAsync = true to prevent ExpressionChangedAfterItHasBeenCheckedError
  // tslint:disable-next-line:no-output-native
  @Output() load = new EventEmitter<void>(true);

  constructor() { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    const loadingChange = changes.loading;
    if (loadingChange) {
      if (loadingChange.currentValue) {
        this.setLoading(true);
        this.error = null;
        loadingChange.currentValue.subscribe(() => this.onSuccess(), (error) => this.onError(error));
      } else {
        this.setLoading(false);
        this.error = null;
      }
    }
  }

  setLoading(value: boolean) {
    this.isLoading = value;
    this.stateChange.emit(value);
  }

  private onSuccess() {
    this.setLoading(false);
    this.load.emit();
  }

  private onError(error: any) {
    this.setLoading(false);
    this.error = error;
  }

  getErrorMessage(): string {
    return this.error ? `An error has occured: ${this.error.message}` : '';
  }

}
