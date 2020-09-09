import { Component, OnInit } from '@angular/core';
import { EntityService } from 'src/app/shared/models/entity/entity.service';
import { MatTableDataSource } from '@angular/material/table';
import { EntitiesWithPagination } from 'src/app/shared/models/entity/entities-with-pagination.model';
import { getEntityDetailsTabs, getEntityDisplayName, getPendingChangesTabs } from '../entity-display-names';
import { MatDialog } from '@angular/material/dialog';
import { take } from 'rxjs/operators';
import { Entity } from 'src/app/shared/models/entity/entity.model';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { ChangeControl } from 'src/app/shared/models/changeControl/change-control.model';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { ConfirmDialogConfig } from 'src/app/shared/components/confirm-dialog/confirm-dialog-config.model';
import { get } from 'lodash';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { ApprovingDialogComponent } from 'src/app/shared/components/approving-dialog/approving-dialog.component';
import { DeleteDialogComponent } from 'src/app/shared/components/delete-dialog/delete-dialog.component';
import { getApiErrorMessage, ErrorMessage } from 'src/app/core/utils/error-template';
import { AuthService } from 'src/app/core/auth/auth.service';
import { ERROR_MESSAGES } from 'src/app/core/constants/error-messages';

@Component({
  selector: 'app-entity-search',
  templateUrl: './entity-search.component.html',
  styleUrls: ['./entity-search.component.scss']
})
export class EntitySearchComponent implements OnInit {

  getEntityDisplayName = getEntityDisplayName;
  ROUTING_PATHS = ROUTING_PATHS;

  entityNameSearchingValue = '';
  serviceSearchingValue = '';

  isLoading = true;
  errorMessage: ErrorMessage;

  entities: EntitiesWithPagination;
  displayedColumns: string[] = ['action', 'changes', 'entity', 'service'];
  dataSource: MatTableDataSource<Entity | ChangeControl>;

  pageIndex = 0;
  pageSize = 100;
  pageSizeOptions: number[] = [5, 10, 20, 50, 100];

  constructor(
    private entityService: EntityService,
    private dialog: MatDialog,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    if (window.history.state.pageIndex && window.history.state.pageSize) {
      this.pageIndex = window.history.state.pageIndex;
      this.pageSize = window.history.state.pageSize;
    }

    this.entityNameSearchingValue = window.history.state.entityNameSearchingValue || '';
    this.serviceSearchingValue = window.history.state.serviceSearchingValue || '';

    this.getEntityList(this.pageIndex, this.pageSize);
  }

  getEntityList(pageIndex: number, pageSize: number) {
    this.isLoading = true;
    this.entityService.getEntityList(removeEmpties({
      entity: this.entityNameSearchingValue || null,
      service: this.serviceSearchingValue || null,
      page: pageIndex.toString(),
      size: pageSize.toString()
    })).pipe(take(1)).subscribe((data: EntitiesWithPagination) => {
      this.isLoading = false;
      this.pageIndex = pageIndex;
      this.pageSize = pageSize;
      this.entities = data;
      this.updateTable();
    },
      error => {
        this.isLoading = false;
        this.errorMessage = getApiErrorMessage(error);
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
      this.dialog.open(ApprovingDialogComponent, new DetailsDialogConfig({
        title: `Change Record: Pending`,
        tabs: getPendingChangesTabs(changeCtrl),
        displayName: getEntityDisplayName
      })));
  }

  openEntityDetailsDialog(entity: Entity) {
    this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
      title: `${entity.service}: ${entity.entity}`,
      tabs: getEntityDetailsTabs(entity),
      displayName: getEntityDisplayName
    }));
  }

  openApprovingDialog(changeControl: ChangeControl) {
    this.addEntityBeforeToChangeControl(changeControl).then(changeCtrl =>
      this.dialog.open(ApprovingDialogComponent, new DetailsDialogConfig({
        title: 'Approve Change',
        tabs: getPendingChangesTabs(changeCtrl),
        displayName: getEntityDisplayName,
        actionData: {
          changeID: changeControl.changeID,
          changer: changeControl.changer,
          errorMessage: {
            message: (this.authService.isTheSameUser(changeCtrl.changer) ? ERROR_MESSAGES.approvingChanges : undefined),
            errors: get(changeCtrl, 'errors')
          },
          approveAction:
            (params: { changeID: string, status: string, approverComments: string }) => this.entityService.resolveChange(params)
        }
      })).afterClosed().subscribe(data => {
        if (get(data, 'refreshList')) {
          this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
            title: `Entity ${get(data, 'status').toLowerCase()}`,
            text: `Entity ${changeControl.entityLog.entity} has been ${get(data, 'status').toLowerCase()}`,
            shouldHideYesCaption: true,
            noCaption: 'Back'
          })).afterClosed().subscribe(() => {
            this.getEntityList(this.pageIndex, this.pageSize);
          });
        }
      }));
  }

  deleteEntity(entity: Entity) {
    this.dialog.open(DeleteDialogComponent, new DetailsDialogConfig({
      title: `Delete ${entity.entity}`,
      tabs: getEntityDetailsTabs(entity),
      displayName: getEntityDisplayName,
      actionData: {
        id: entity.entityId,
        deleteAction: (id: string, changerComments: string) => this.entityService.deleteEntity(id, changerComments)
      }
    })).afterClosed().subscribe(data => {
      if (get(data, 'refreshList')) {
        this.dialog.open(ConfirmDialogComponent, new ConfirmDialogConfig({
          title: `Entity deleted`,
          text: `Entity ${entity.entity} has been deleted`,
          shouldHideYesCaption: true,
          noCaption: 'Back'
        })).afterClosed().subscribe(() => {
          this.getEntityList(this.pageIndex, this.pageSize);
        });
      }
    });
  }

}
