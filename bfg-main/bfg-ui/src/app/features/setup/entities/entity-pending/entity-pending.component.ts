import { Component, OnInit } from '@angular/core';
import { Entity } from 'src/app/shared/entity/entity.model';
import { EntityService } from 'src/app/shared/entity/entity.service';
import { MatTableDataSource } from '@angular/material/table';
import { take } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';
import { ENTITY_DISPLAY_NAMES, getEntityDetailsFields, getPendingChangesFields } from '../entity-display-names';
import { EntityApprovingDialogComponent } from '../entity-approving-dialog/entity-approving-dialog.component';
import { ChangeControl } from 'src/app/shared/entity/change-control.model';
import { get } from 'lodash';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { ConfirmDialogConfig } from 'src/app/shared/components/confirm-dialog/confirm-dialog-config.model';
import { ChangeControlsWithPagination } from 'src/app/shared/entity/change-controls-with-pagination.model';

@Component({
  selector: 'app-entity-pending',
  templateUrl: './entity-pending.component.html',
  styleUrls: ['./entity-pending.component.scss']
})
export class EntityPendingComponent implements OnInit {

  entityDisplayNames = ENTITY_DISPLAY_NAMES;

  isLoading = true;
  changeControls: ChangeControlsWithPagination;
  displayedColumns: string[] = ['action', 'changes', 'entity', 'service'];
  dataSource: MatTableDataSource<ChangeControl>;

  pageIndex = 0;
  pageSize = 10;
  pageSizeOptions: number[] = [5, 10, 20];

  constructor(
    private entityService: EntityService,
    private dialog: MatDialog
  ) { }

  ngOnInit() {
    this.getPendingChanges(this.pageIndex, this.pageSize);
  }

  getPendingChanges(pageIndex: number, pageSize: number) {
    this.isLoading = true;
    this.entityService.getPendingChanges({ page: pageIndex.toString(), size: pageSize.toString() })
      .pipe(take(1)).subscribe((data: ChangeControlsWithPagination) => {
        this.isLoading = false;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.changeControls = data;
        this.updateTable();
      });
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.changeControls.content);
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
            title: `Entity ${get(data, 'status').toLowerCase()}`,
            text: `Entity ${changeControl.entityLog.entity} has been ${get(data, 'status').toLowerCase()}`,
            shouldHideYesCaption: true,
            noCaption: 'Back'
          })).afterClosed().subscribe(() => {
            this.getPendingChanges(this.pageIndex, this.pageSize);
          });
        }
      }));
  }

}
