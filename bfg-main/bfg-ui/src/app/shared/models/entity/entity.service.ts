import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Entity } from './entity.model';
import { Observable } from 'rxjs';
import { ChangeControl } from '../changeControl/change-control.model';
import { EntitiesWithPagination } from './entities-with-pagination.model';
import { ChangeControlsWithPagination } from '../changeControl/change-controls-with-pagination.model';
import { ChangeResolution } from '../changeControl/change-resolution.model';
import { MQDetails } from './mq-details.model';
import { removeEmpties } from '../../utils/utils';

@Injectable({
  providedIn: 'root'
})
export class EntityService {

  private apiUrl: string = environment.apiUrl + 'entities/';

  constructor(private http: HttpClient) {
  }

  createEntity(entity: Entity) {
    return this.http.post<Entity>(this.apiUrl, entity);
  }

  editEntity(entity: Entity) {
    return this.http.put<Entity>(this.apiUrl + entity.entityId, entity);
  }

  deleteEntity(entityId: string, changerComments: string) {
    return this.http.delete(this.apiUrl + entityId, { params: changerComments && { changerComments } });
  }

  getEntityList(params?: { entity?: string; service?: string; swiftDN?: string; page?: string; size?: string }): Observable<EntitiesWithPagination> {
    return this.http.get<EntitiesWithPagination>(this.apiUrl, { params });
  }

  getEntityById(entityId: string) {
    return this.http.get<Entity>(this.apiUrl + entityId);
  }

  getPendingEntityById(changeId) {
    return this.http.get<Entity>(this.apiUrl + 'pending/' + changeId);
  }

  getPendingChangeById(changeId) {
    return this.http.get<ChangeControl>(this.apiUrl + 'changeControl/' + changeId);
  }

  deletePendingChange(changeId) {
    return this.http.delete(this.apiUrl + 'pending/' + changeId, { responseType: 'text' });
  }

  editPendingEntity(changeId, entity: Entity) {
    return this.http.put<Entity>(this.apiUrl + 'pending/' + changeId, entity);
  }

  getPendingChanges(params?: { entity?: string; service?: string; swiftDN?: string; page?: string; size?: string }): Observable<EntitiesWithPagination> {
    return this.http.get<ChangeControlsWithPagination>(this.apiUrl + 'pending', { params });
  }

  resolveChange(resolution: ChangeResolution) {
    return this.http.post(this.apiUrl + 'pending', resolution);
  }

  isEntityExists(service: string, entity: string): Observable<boolean> {
    return this.http.get<boolean>(this.apiUrl + 'existence/entity-service', {
      params: {
        service,
        entity
      }
    });
  }

  isMailboxPathOutExists(mailboxPathOut: string): Observable<boolean> {
    return this.http.get<boolean>(this.apiUrl + 'existence/mailbox', {
      params: {
        mailboxPathOut
      }
    });
  }

  isMqQueueOutExists(mqQueueOut: string): Observable<boolean> {
    return this.http.get<boolean>(this.apiUrl + 'existence/queue', {
      params: {
        mqQueueOut
      }
    });
  }

  isRouteAttributesExists(params: {
    inboundRequestorDN: string,
    inboundResponderDN: string,
    inboundService: string,
    inboundRequestType: string[]
  }): Observable<boolean> {
    return this.http.get<boolean>(this.apiUrl + 'existence/route-attributes', { params });
  }

  getScheduleFileTypes() {
    return this.http.get<string[]>(this.apiUrl + 'file-type');
  }

  getInboundRequestTypes() {
    return this.http.get<string[]>(this.apiUrl + 'inbound-request-type');
  }

  getMQDetails() {
    return this.http.get<MQDetails>(this.apiUrl + 'mq-details');
  }

  transmitEntity(entityID: string, fileType: string, password: string) {
    return this.http.post(this.apiUrl + 'transmit', { entityID, fileType, password });
  }

  getInboundService() {
    return this.http.get(this.apiUrl + 'inbound-service', { responseType: 'text' });
  }

  getSWIFTService() {
    return this.http.get(this.apiUrl + 'swift-service', { responseType: 'text' });
  }	

  getDirectParticipantList(id?) {
    return this.http.get<string[]>(this.apiUrl + 'participants', { params: removeEmpties({ id }) });
  }
}
