import { Component, OnInit, OnDestroy } from '@angular/core';
import { Entity } from 'src/app/shared/entity/entity.model';
import { EntityService } from 'src/app/shared/entity/entity.service';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import { EntitiesWithPagination } from 'src/app/shared/entity/entities-with-pagination.model';
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-entity-pending',
  templateUrl: './entity-pending.component.html',
  styleUrls: ['./entity-pending.component.scss']
})
export class EntityPendingComponent implements OnInit {
  entities: Entity[] = [];
  displayedColumns: string[] = ['action', 'changes', 'entity', 'service'];
  dataSource: MatTableDataSource<Entity>;
  entitiesSubscription: Subscription;

  constructor(private entityService: EntityService) { }

  ngOnInit() {
    this.entityService.getEntites().pipe(take(1)).subscribe((data: EntitiesWithPagination) => {
      this.entities = data.content;
      this.updateTable();
    });
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.entities);
  }

}
