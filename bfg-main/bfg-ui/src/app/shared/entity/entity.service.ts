import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Entity } from './entity.model';
import { Observable } from 'rxjs';

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

  getEntites<EntitiesWithPagination>() {
    return this.http.get(this.apiUrl);
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
