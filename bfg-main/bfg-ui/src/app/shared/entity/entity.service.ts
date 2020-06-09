import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Entity } from './entity.model';
import { Observable } from 'rxjs';
import { ChangeControl } from './change-control.model';

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

  getPendingChanges(): Observable<ChangeControl[]> {
    return this.http.get<ChangeControl[]>(this.apiUrl + 'pending');
  }

  resolveChange(resolution: {changeID: string, status: string, approverComments: string}){
    return this.http.post(this.apiUrl + 'pending', resolution);
  }

  isEntityExists(service: string, entity: string): Observable<boolean>{
    return this.http.get<boolean>(this.apiUrl + 'existence', {
      params: {
        service,
        entity
      }
    });
  }

}