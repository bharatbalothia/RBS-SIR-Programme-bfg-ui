import { Component, OnInit } from '@angular/core';
import { get } from 'lodash';
import { TooltipService } from 'src/app/shared/components/tooltip/tooltip.service';
import { AuditEventCriteria } from 'src/app/shared/models/audit-event/audit-event-criteria.model';
import { AuditEvent } from 'src/app/shared/models/audit-event/audit-event.model';
import { AuditEventService } from 'src/app/shared/models/audit-event/audit-event.service';
import { removeEmpties, removeFirst } from 'src/app/shared/utils/utils';

@Component({
  selector: 'app-home-events',
  templateUrl: './home-events.component.html',
  styleUrls: ['./home-events.component.scss']
})

export class HomeEventsComponent implements OnInit {

  size = 40;
  isLoading = false;

  auditEvents: AuditEvent[] = [];
  auditEventCriteriaData: AuditEventCriteria;

  objectActedOn = '';
  username = '';
  action = '';
  actionType = '';
  eventTypes = [];

  shouldHideEvents: boolean;

  constructor(
    private auditEventService: AuditEventService,
    private toolTip: TooltipService,
  ) { }

  ngOnInit(): void {
    this.getEventCriteriaData();
  }

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

  refreshAuditEvents = () => {
    this.auditEvents = [];
    this.getAuditEvents();
  }

  getAuditEvents = (id?: string) =>
    this.auditEventService.getAuditEvents(removeEmpties({
      id,
      size: this.size,
      objectActedOn: this.objectActedOn || null,
      username: this.username || null,
      action: this.action || null,
      actionType: this.actionType || null,
      eventType: this.eventTypes.length > 0 ? this.eventTypes : null,
    })).pipe(data => this.setLoading(data)).subscribe((data: AuditEvent[]) => {
      this.auditEvents = this.auditEvents.concat(data);
      this.isLoading = false;
    },
      () => this.isLoading = false
    )

  getEventCriteriaData = () =>
    this.auditEventService.getEventCriteriaData().pipe(data => this.setLoading(data)).subscribe((data: AuditEventCriteria) => {
      this.auditEventCriteriaData = data;
      this.eventTypes = [...this.getEventTypeCriteriaData()];
      this.getAuditEvents();
    },
      () => {
        this.isLoading = false;
        this.shouldHideEvents = true;
      }
    )

  getEventTypeCriteriaData = () => get(this.auditEventCriteriaData, 'eventType', []);

  getLastAuditEventId = () => this.auditEvents.length > 0 && this.auditEvents[this.auditEvents.length - 1].id;

  clearParams = () => {
    this.objectActedOn = '';
    this.username = '';
    this.action = '';
    this.actionType = '';
    this.eventTypes = [...this.getEventTypeCriteriaData()];
    this.refreshAuditEvents();
    return false;
  }

  clearField = (event, field) => {
    this[field] = '';
    this.refreshAuditEvents();
    event.stopPropagation();
  }

  isClearActive = () =>
    this.objectActedOn !== ''
    || this.username !== ''
    || this.action !== ''
    || this.actionType !== ''
    || this.eventTypes.length !== this.getEventTypeCriteriaData().length

  onEventTypeRemoved = (eventType: string) => {
    const eventTypes = this.eventTypes;
    removeFirst(eventTypes, eventType);
    this.eventTypes = eventTypes.length ? [...eventTypes] : [...this.getEventTypeCriteriaData()];
    this.refreshAuditEvents();
  }

  onEventTypeSelection = () => {
    if (!this.eventTypes.length) {
      this.eventTypes = [...this.getEventTypeCriteriaData()];
    }
  }

  shouldDisableActionOrActionType = () => this.eventTypes.length === 1 && this.eventTypes[0] === 'transmit';

  getTooltip(step: string, field: string): string {
    const toolTip = this.toolTip.getTooltip({
      type: 'home',
      qualifier: step,
      mode: 'search',
      fieldName: field
    });
    return toolTip.length > 0 ? toolTip : field;
  }
}
