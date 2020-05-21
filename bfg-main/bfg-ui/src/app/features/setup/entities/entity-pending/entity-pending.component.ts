import { Component, OnInit, OnDestroy } from '@angular/core';
import { Entity } from 'src/app/shared/entity/entity.model';
import { EntityService } from 'src/app/shared/entity/entity.service';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription, Observable } from 'rxjs';
import { EntitiesWithPagination } from 'src/app/shared/entity/entities-with-pagination.model';
import { take } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';
import { ENTITY_DISPLAY_NAMES, getEntityDisplayName, getEntityDetailsFields } from '../entity-display-names';

@Component({
  selector: 'app-entity-pending',
  templateUrl: './entity-pending.component.html',
  styleUrls: ['./entity-pending.component.scss']
})
export class EntityPendingComponent implements OnInit {

  entityDisplayNames = ENTITY_DISPLAY_NAMES;

  isLoading = true;
  entities: Entity[] = [];
  displayedColumns: string[] = ['action', 'changes', 'entity', 'service'];
  dataSource: MatTableDataSource<Entity>;

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
    const modref = this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
      title: `Change Record: Pending`,
      sections: [
        {
          sectionTitle: 'Change Details',
          sectionItems: [
            { fieldName: 'Change ID', fieldValue: '7385s5sd4fsdfsd' },
            { fieldName: 'Object type', fieldValue: 'Entity' },
            { fieldName: 'Before Changes', fieldValue: 'None' },
            { fieldName: 'Operation', fieldValue: 'CREATE' },
            { fieldName: 'Status', fieldValue: 'Pending' },
          ]
        }
      ],
    }));
    modref.componentInstance.body = `<input matInput type="text">`;
  }

  openEntityDetailsDialog(entity: Entity) {
    this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
      title: `${entity.service}: ${entity.entity}`,
      sections: getEntityDetailsFields(entity),
    }));
  }
}
