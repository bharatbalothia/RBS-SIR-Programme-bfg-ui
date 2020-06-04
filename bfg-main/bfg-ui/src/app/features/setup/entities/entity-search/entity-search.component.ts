import { Component, OnInit } from '@angular/core';
import { EntityService } from 'src/app/shared/entity/entity.service';
import { MatTableDataSource } from '@angular/material/table';
import { EntitiesWithPagination } from 'src/app/shared/entity/entities-with-pagination.model';
import { getEntityDetailsFields, getEntityDisplayName, getPendingChangesFields } from '../entity-display-names';
import { MatDialog } from '@angular/material/dialog';
import { take } from 'rxjs/operators';
import { Entity } from 'src/app/shared/entity/entity.model';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { ChangeControl } from 'src/app/shared/entity/change-control.model';
import { EntityApprovingDialogComponent } from '../entity-approving-dialog/entity-approving-dialog.component';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { ConfirmDialogConfig } from 'src/app/shared/components/confirm-dialog/confirm-dialog-config.model';
import { get } from 'lodash';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';

@Component({
  selector: 'app-entity-search',
  templateUrl: './entity-search.component.html',
  styleUrls: ['./entity-search.component.scss']
})
export class EntitySearchComponent implements OnInit {

  getEntityDisplayName = getEntityDisplayName;
  ROUTING_PATHS = ROUTING_PATHS;

  searchByItems: string[] = ['entity', 'service'];
  selectedSearchByItem: string;
  searchingValue = '';

  isLoading = true;
  entities: EntitiesWithPagination;
  displayedColumns: string[] = ['action', 'changes', 'entity', 'service'];
  dataSource: MatTableDataSource<Entity | ChangeControl>;

  pageIndex = 0;
  pageSize = 10;
  pageSizeOptions: number[] = [5, 10, 20];

  constructor(
    private entityService: EntityService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    if (window.history.state.pageIndex && window.history.state.pageSize) {
      this.pageIndex = window.history.state.pageIndex;
      this.pageSize = window.history.state.pageSize;
    }
    this.getEntityList(this.pageIndex, this.pageSize);
  }

  getEntityList(pageIndex: number, pageSize: number) {
    this.isLoading = true;
    this.entityService.getEntityList(removeEmpties({
      [this.selectedSearchByItem]: this.searchingValue !== '' ? this.searchingValue : null,
      page: pageIndex.toString(),
      size: pageSize.toString()
    })).pipe(take(1)).subscribe((data: EntitiesWithPagination) => {
      this.isLoading = false;
      this.pageIndex = pageIndex;
      this.pageSize = pageSize;
      this.entities = data;
      this.updateTable();
    });
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.entities.content);
  }

  addEntityBeforeToChangeControl(changeControl: ChangeControl): Promise<ChangeControl> {
    const entityId = get(changeControl.entityLog, 'entityId');
    if (entityId) {
      return this.entityService.getEntityById(entityId.toString()).toPromise()
        .then(data => ({ ...changeControl, entityBefore: data }));
    }
    else {
      return new Promise((res) => res(changeControl));
    }
  }

  openInfoDialog(changeControl: ChangeControl) {
    this.addEntityBeforeToChangeControl(changeControl).then(changeCtrl =>
      this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
        title: `Change Record: Pending`,
        tabs: getPendingChangesFields(changeCtrl),
      })));
  }

  openEntityDetailsDialog(entity: Entity) {
    this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
      title: `${entity.service}: ${entity.entity}`,
      tabs: getEntityDetailsFields(entity),
    }));
  }

  openApprovingDialog(changeControl: ChangeControl) {
    this.addEntityBeforeToChangeControl(changeControl).then(changeCtrl =>
      this.dialog.open(EntityApprovingDialogComponent, new DetailsDialogConfig({
        title: 'Approve Change',
        tabs: getPendingChangesFields(changeCtrl),
        actionData: { changeID: changeControl.changeID }
      })).afterClosed().subscribe(data => {
        if (get(data, 'refreshList')) {
          this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
            title: `Entity saved`,
            text: `Entity ${changeControl.entityLog.entity} has been saved`,
            shouldHideYesCaption: true,
            noCaption: 'Back'
          })).afterClosed().subscribe(() => {
            this.getEntityList(this.pageIndex, this.pageSize);
          });
        }
      }));
  }

  onSearchByItemSelect(searchByItem: string) {
    this.selectedSearchByItem = searchByItem;
    this.searchingValue = '';
  }

}
