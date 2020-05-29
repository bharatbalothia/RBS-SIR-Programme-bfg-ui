import { Component, OnInit } from '@angular/core';
import { EntityService } from 'src/app/shared/entity/entity.service';
import { MatTableDataSource } from '@angular/material/table';
import { EntitiesWithPagination } from 'src/app/shared/entity/entities-with-pagination.model';
import { ENTITY_DISPLAY_NAMES, getEntityDetailsFields } from '../entity-display-names';
import { MatDialog } from '@angular/material/dialog';
import { take } from 'rxjs/operators';
import { Entity } from 'src/app/shared/entity/entity.model';
import { PageEvent } from '@angular/material/paginator';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';

@Component({
  selector: 'app-entity-search',
  templateUrl: './entity-search.component.html',
  styleUrls: ['./entity-search.component.scss']
})
export class EntitySearchComponent implements OnInit {

  entityDisplayNames = ENTITY_DISPLAY_NAMES;

  isLoading = true;
  entities: EntitiesWithPagination;
  displayedColumns: string[] = ['action', 'changes', 'entity', 'service'];
  dataSource: MatTableDataSource<Entity>;

  pageIndex = 0;
  pageSize = 5;
  pageSizeOptions: number[] = [5, 10, 20];

  constructor(
    private entityService: EntityService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.getEntityList(this.pageIndex, this.pageSize);
  }

  getEntityList(pageIndex, pageSize) {
    this.entityService.getEntityList({ page: pageIndex.toString(), size: pageSize.toString() })
      .pipe(take(1)).subscribe((data: EntitiesWithPagination) => {
        this.isLoading = false;
        this.entities = data;
        this.updateTable();
      });
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.entities.content);
  }

  openEntityDetailsDialog(entity: Entity) {
    this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
      title: `${entity.service}: ${entity.entity}`,
      sections: getEntityDetailsFields(entity),
    }));
  }

}
