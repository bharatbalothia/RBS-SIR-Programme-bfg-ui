import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AutoRefreshService {

    shouldAutoRefresh: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

    constructor() {
    }

    setAutoRefresh(value: boolean) {
        this.shouldAutoRefresh.next(value);
    }
}
