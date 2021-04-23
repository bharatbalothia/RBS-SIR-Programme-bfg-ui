import { Component, Input, OnInit } from '@angular/core';
import { AuditEvent } from 'src/app/shared/models/audit-event/audit-event.model';
import { AUDIT_EVENT_TYPES } from 'src/app/shared/models/audit-event/audit-events-constants';

@Component({
  selector: 'app-home-event',
  templateUrl: './home-event.component.html',
  styleUrls: ['./home-event.component.scss']
})
export class HomeEventComponent implements OnInit {

  AUDIT_EVENT_TYPES = AUDIT_EVENT_TYPES;

  @Input() event: AuditEvent;

  constructor() { }

  ngOnInit(): void {
  }

}
