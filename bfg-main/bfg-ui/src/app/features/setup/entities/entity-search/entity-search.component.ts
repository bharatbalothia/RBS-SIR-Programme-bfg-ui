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
import { get } from 'lodash';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { ApprovingDialogComponent } from 'src/app/shared/components/approving-dialog/approving-dialog.component';
import { DeleteDialogComponent } from 'src/app/shared/components/delete-dialog/delete-dialog.component';
import { AuthService } from 'src/app/core/auth/auth.service';
import { ERROR_MESSAGES } from 'src/app/core/constants/error-messages';
import { ENTITY_SERVICE_TYPE } from 'src/app/shared/models/entity/entity-constants';
import { TransmitDialogComponent } from 'src/app/shared/components/transmit-dialog/transmit-dialog.component';
import { Router } from '@angular/router';
import { CHANGE_OPERATION } from 'src/app/shared/models/changeControl/change-operation';
import { NotificationService } from 'src/app/shared/services/notification.service';

@Component({
  selector: 'app-entity-search',
  templateUrl: './entity-search.component.html',
  styleUrls: ['./entity-search.component.scss']
})
export class EntitySearchComponent implements OnInit {

  getEntityDisplayName = getEntityDisplayName;
  ROUTING_PATHS = ROUTING_PATHS;
  SERVICE_TYPE = ENTITY_SERVICE_TYPE;
  CHANGE_OPERATION = CHANGE_OPERATION;

  entityNameSearchingValue = '';
  serviceSearchingValue = '';
  DNSearchingValue = '';

  isLoading = true;
  isLoadingDetails = false;

  entities: EntitiesWithPagination;
  displayedColumns: string[] = ['action', 'changes', 'entity', 'service'];
  dataSource: MatTableDataSource<Entity | ChangeControl>;

  pageIndex = 0;
  pageSize = 100;
  pageSizeOptions: number[] = [5, 10, 20, 50, 100];

  constructor(
    private entityService: EntityService,
    private dialog: MatDialog,
    private authService: AuthService,
    private router: Router,
    private notificationService: NotificationService,
  ) { }

  ngOnInit(): void {
    if (window.history.state.pageIndex && window.history.state.pageSize) {
      this.pageIndex = window.history.state.pageIndex;
      this.pageSize = window.history.state.pageSize;
    }

    this.entityNameSearchingValue = window.history.state.entityNameSearchingValue || '';
    this.serviceSearchingValue = window.history.state.serviceSearchingValue || '';
    this.DNSearchingValue = window.history.state.DNSearchingValue || '';

    this.getEntityList(this.pageIndex, this.pageSize);
  }

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

  getEntityList(pageIndex: number, pageSize: number) {
    this.entityService.getEntityList(removeEmpties({
      entity: this.entityNameSearchingValue || null,
      service: this.serviceSearchingValue || null,
      swiftDN: this.DNSearchingValue || null,
      page: pageIndex.toString(),
      size: pageSize.toString()
    })).pipe(take(1)).pipe(data => this.setLoading(data)).subscribe((data: EntitiesWithPagination) => {
      this.isLoading = false;
      this.pageIndex = pageIndex;
      this.pageSize = pageSize;
      this.entities = data;
      this.updateTable();
    },
      error => this.isLoading = false
    );
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.entities.content);
  }

  addEntityBeforeToChangeControl(changeControl: ChangeControl) {
    const entityId = get(changeControl.entityLog, 'entityId');
    if (entityId) {
      this.isLoadingDetails = true;
      return this.entityService.getEntityById(entityId.toString()).toPromise()
        .then(data => {
          this.isLoadingDetails = false;
          return ({ ...changeControl, entityBefore: data });
        }).finally(() => {
          this.isLoadingDetails = false;
        });
    }
    else {
      return new Promise((res) => res(changeControl));
    }
  }

  getEntityDetails = (entity: Entity) => {
    this.isLoadingDetails = true;
    return this.entityService.getEntityById(entity.entityId).toPromise()
      .finally(() => {
        this.isLoadingDetails = false;
      });
  }

  getPendingEntityDetails = (changeControl: ChangeControl) => {
    this.isLoadingDetails = true;
    return this.entityService.getPendingChangeById(changeControl.changeID).toPromise()
      .finally(() => {
        this.isLoadingDetails = false;
      });
  }

  openInfoDialog(changeControl: ChangeControl) {
    const getChangeControl = () => this.getPendingEntityDetails(changeControl)
      .then((data: ChangeControl) => data && this.addEntityBeforeToChangeControl(data));

    getChangeControl().then((changeCtrl: ChangeControl) =>
      changeCtrl && this.dialog.open(ApprovingDialogComponent, new DetailsDialogConfig({
        title: `Change Record: Pending`,
        data: changeCtrl,
        getData: getChangeControl,
        getTabs: getPendingChangesTabs,
        displayName: getEntityDisplayName
      })));
  }

  openDetailsDialog(value: ChangeControl | Entity) {
    let detailsData = null;
    if (value.changeID) {
      if (!(value as ChangeControl).entityLog.entityId) {
        return;
      }
      detailsData = () => this.getEntityDetails((value as ChangeControl).entityLog);
    }
    else {
      detailsData = () => this.getEntityDetails(value as Entity);
    }
    detailsData().then((entity: Entity) => entity &&
      this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
        getTitle: (data: Entity) => `${data.service}: ${data.entity}`,
        data: entity,
        getData: detailsData,
        getTabs: getEntityDetailsTabs,
        displayName: getEntityDisplayName
      })));
  }

  openApprovingDialog(changeControl: ChangeControl) {
    const getChangeControl = () => this.getPendingEntityDetails(changeControl)
      .then((detailedChangeCtrl: ChangeControl) =>
        detailedChangeCtrl && this.addEntityBeforeToChangeControl(detailedChangeCtrl));

    getChangeControl().then((changeCtrl: ChangeControl) => changeCtrl &&
      this.dialog.open(ApprovingDialogComponent, new DetailsDialogConfig({
        title: 'Approve Change',
        yesCaption: 'Cancel',
        getData: getChangeControl,
        data: changeCtrl,
        getTabs: (data: ChangeControl) => getPendingChangesTabs(data, true),
        displayName: getEntityDisplayName,
        actionData: {
          changeID: changeControl.changeID,
          changer: changeControl.changer,
          errorMessage: {
            message: null,
            errors: get(changeCtrl, 'errors'),
          },
          warningMessage: this.authService.isTheSameUser(changeCtrl.changer) && ERROR_MESSAGES.approvingChanges,
          shouldDisableApprove: this.authService.isTheSameUser(changeCtrl.changer),
          approveAction:
            (params: { changeID: string, status: string, approverComments: string }) => this.entityService.resolveChange(params)
        }
      })).afterClosed().subscribe(data => {
        if (get(data, 'refreshList')) {
          this.notificationService.show(
            `Entity ${get(data, 'status').toLowerCase()}`,
            `Entity ${changeControl.entityLog.entity} has been ${get(data, 'status').toLowerCase()}`,
            'success'
          );
          this.getEntityList(this.pageIndex, this.pageSize);
        }
      }));
  }

  deleteEntity(entity: Entity) {
    const getEntityDetails = () => this.getEntityDetails(entity);
    getEntityDetails().then((detailedEntity: Entity) => this.dialog.open(DeleteDialogComponent, new DetailsDialogConfig({
      title: `Delete ${detailedEntity.entity}`,
      yesCaption: 'Cancel',
      data: detailedEntity,
      getData: getEntityDetails,
      getTabs: (data: Entity) => getEntityDetailsTabs({ ...data, operation: CHANGE_OPERATION.DELETE }),
      displayName: getEntityDisplayName,
      actionData: {
        id: detailedEntity.entityId,
        deleteAction: (id: string, changerComments: string) => this.entityService.deleteEntity(id, changerComments)
      }
    })).afterClosed().subscribe(data => {
      if (get(data, 'refreshList')) {
        this.notificationService.show(
          `Entity deleted`,
          `The update to the Entity will be committed after the change has been approved.`,
          'success'
        );
        this.getEntityList(this.pageIndex, this.pageSize);
      }
    }));
  }

  deletePendingChange(changeControl: ChangeControl) {
    const getPendingEntityDetails = () => this.getPendingEntityDetails(changeControl)
      .then((data: ChangeControl) => data && this.addEntityBeforeToChangeControl(data));

    getPendingEntityDetails().then((changeCtrl: ChangeControl) =>
      changeCtrl && this.dialog.open(DeleteDialogComponent, new DetailsDialogConfig({
        getTitle: (data: ChangeControl) => `Delete ${data.changeID}`,
        data: changeCtrl,
        getData: getPendingEntityDetails,
        yesCaption: 'Cancel',
        getTabs: getPendingChangesTabs,
        displayName: getEntityDisplayName,
        actionData: {
          id: changeCtrl.changeID,
          shouldHideComments: true,
          deleteAction: (id: string) => this.entityService.deletePendingChange(id)
        }
      })).afterClosed().subscribe(data => {
        if (get(data, 'refreshList')) {
          this.notificationService.show(
            `Pending Change deleted`,
            `The Pending change ${changeCtrl.changeID} has been deleted.`,
            'success'
          );
          this.getEntityList(this.pageIndex, this.pageSize);
        }
      }));
  }

  transmitEntity(entity: Entity) {
    this.dialog.open(TransmitDialogComponent, new DetailsDialogConfig({
      title: `Transmit File for the Entity ID ${entity.entity}`,
      actionData: {
        entity,
        transmitAction: (id: string, fileType: string, password: string) => this.entityService.transmitEntity(id, fileType, password)
      }
    }));
  }

  getCurrentRoute = () => this.router.url;

  isTheSameUser = (user) => this.authService.isTheSameUser(user);

  clearParams = () => {
    this.entityNameSearchingValue = '';
    this.serviceSearchingValue = '';
    this.DNSearchingValue = '';
    this.getEntityList(0, this.pageSize);
    return false;
  }

  isClearActive = () =>
    this.entityNameSearchingValue !== '' || this.serviceSearchingValue !== '' || this.DNSearchingValue !== ''
}
