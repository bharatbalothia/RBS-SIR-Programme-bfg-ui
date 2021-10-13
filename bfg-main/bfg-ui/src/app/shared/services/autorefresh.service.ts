import { EventEmitter, Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class AutoRefreshService {

    shouldAutoRefresh: EventEmitter<boolean> = new EventEmitter<boolean>();

    constructor() {
    }

    setAutoRefresh(value: boolean) {
        this.shouldAutoRefresh.emit(value);
    }
}
