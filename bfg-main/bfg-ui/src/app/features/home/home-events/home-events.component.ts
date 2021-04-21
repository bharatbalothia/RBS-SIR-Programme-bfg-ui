import { Component, OnInit } from '@angular/core';
import { AuditEventCriteria } from 'src/app/shared/models/audit-event/audit-event-criteria.model';
import { AuditEvent } from 'src/app/shared/models/audit-event/audit-event.model';
import { AuditEventService } from 'src/app/shared/models/audit-event/audit-event.service';

@Component({
  selector: 'app-home-events',
  templateUrl: './home-events.component.html',
  styleUrls: ['./home-events.component.scss']
})

export class HomeEventsComponent implements OnInit {

  size = 30;
  isLoading = false;

  auditEvents: AuditEvent[] = [];
  auditEventCriteriaData: AuditEventCriteria;

  constructor(
    private auditEventService: AuditEventService
  ) { }

  ngOnInit(): void {
    this.getEventCriteriaData();
  }

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

  getAuditEvents = (id?: string) =>
    this.auditEventService.getAuditEvents({ id, size: this.size }).pipe(data => this.setLoading(data)).subscribe((data: AuditEvent[]) => {
      this.auditEvents = [...this.auditEvents, ...data];
      this.isLoading = false;
    },
      () => this.isLoading = false
    )

  getEventCriteriaData = () =>
    this.auditEventService.getEventCriteriaData().pipe(data => this.setLoading(data)).subscribe((data: AuditEventCriteria) => {
      this.auditEventCriteriaData = data;
      this.getAuditEvents();
    },
      () => this.isLoading = false
    )

  getLastAuditEventId = () => this.auditEvents.length > 0 && this.auditEvents[this.auditEvents.length - 1].id;
}
