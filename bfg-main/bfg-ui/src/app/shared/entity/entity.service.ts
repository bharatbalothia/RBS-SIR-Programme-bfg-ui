import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Entity } from './entity.model';
import { Observable } from 'rxjs';
import { ChangeControl } from './change-control.model';
import { EntitiesWithPagination } from './entities-with-pagination.model';
import { ChangeControlsWithPagination } from './change-controls-with-pagination.model';

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

  getEntityList(params?: { entity?: string; service?: string; page?: string; size?: string }): Observable<EntitiesWithPagination> {
    return this.http.get<EntitiesWithPagination>(this.apiUrl, { params });
  }

  getEntityById(entityId: string) {
    return this.http.get<Entity>(this.apiUrl + entityId);
  }

  getPendingChanges(params?: { page?: string; size?: string }): Observable<EntitiesWithPagination> {
    return this.http.get<ChangeControlsWithPagination>(this.apiUrl + 'pending', { params });
  }

  resolveChange(resolution: { changeID: string, status: string, approverComments: string }) {
    return this.http.post(this.apiUrl + 'pending', resolution);
  }

  isEntityExists(service: string, entity: string): Observable<boolean> {
    return this.http.get<boolean>(this.apiUrl + 'existence', {
      params: {
        service,
        entity
      }
    });
  }

}
