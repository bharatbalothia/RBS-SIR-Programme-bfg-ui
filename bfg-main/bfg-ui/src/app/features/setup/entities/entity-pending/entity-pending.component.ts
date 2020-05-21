import { Component, OnInit, OnDestroy } from '@angular/core';
import { Entity } from 'src/app/shared/entity/entity.model';
import { EntityService } from 'src/app/shared/entity/entity.service';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription, Observable } from 'rxjs';
import { EntitiesWithPagination } from 'src/app/shared/entity/entities-with-pagination.model';
import { take } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';

@Component({
  selector: 'app-entity-pending',
  templateUrl: './entity-pending.component.html',
  styleUrls: ['./entity-pending.component.scss']
})
export class EntityPendingComponent implements OnInit {

  isLoading = true;
  entities: Entity[] = [];
  displayedColumns: string[] = ['action', 'changes', 'entity', 'service'];
  dataSource: MatTableDataSource<Entity>;
  entitiesSubscription: Subscription;

  constructor(
    private entityService: EntityService,
    private dialog: MatDialog
  ) { }

  ngOnInit() {
    this.entityService.getEntities().pipe(take(1)).subscribe((data: EntitiesWithPagination) => {
      this.isLoading = false;
      this.entities = data.content;
      this.updateTable();
    });
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.entities);
  }

  openInfoDialog() {
     this.dialog.open(DetailsDialogComponent, new DetailsDialogComponent({
      title: `Info`,
      sections: [],
    }));
  }
}
