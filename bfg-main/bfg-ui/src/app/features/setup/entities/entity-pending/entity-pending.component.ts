import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { get } from 'lodash';
import { take } from 'rxjs/operators';
import { AuthService } from 'src/app/core/auth/auth.service';
import { ERROR_MESSAGES } from 'src/app/core/constants/error-messages';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { ApprovingDialogComponent } from 'src/app/shared/components/approving-dialog/approving-dialog.component';
import { DeleteDialogComponent } from 'src/app/shared/components/delete-dialog/delete-dialog.component';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';
import { ChangeControl } from 'src/app/shared/models/changeControl/change-control.model';
import { ChangeControlsWithPagination } from 'src/app/shared/models/changeControl/change-controls-with-pagination.model';
import { CHANGE_OPERATION } from 'src/app/shared/models/changeControl/change-operation';
import { Entity } from 'src/app/shared/models/entity/entity.model';
import { EntityService } from 'src/app/shared/models/entity/entity.service';
import { NotificationService } from 'src/app/shared/services/notification.service';
import { removeEmpties } from 'src/app/shared/utils/utils';
import { ENTITY_DISPLAY_NAMES, getEntityDetailsTabs, getEntityDisplayName, getPendingChangesTabs } from '../entity-display-names';

@Component({
  selector: 'app-entity-pending',
  templateUrl: './entity-pending.component.html',
  styleUrls: ['./entity-pending.component.scss']
})
export class EntityPendingComponent implements OnInit {

  ROUTING_PATHS = ROUTING_PATHS;
  entityDisplayNames = ENTITY_DISPLAY_NAMES;
  CHANGE_OPERATION = CHANGE_OPERATION;


  entityNameSearchingValue = '';
  serviceSearchingValue = '';
  DNSearchingValue = '';

  isLoading = true;
  isLoadingDetails = false;

  changeControls: ChangeControlsWithPagination;
  displayedColumns: string[] = ['action', 'entity', 'service'];
  dataSource: MatTableDataSource<ChangeControl>;

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

  ngOnInit() {
    this.getPendingChanges(this.pageIndex, this.pageSize);

    this.entityNameSearchingValue = window.history.state.entityNameSearchingValue || '';
    this.serviceSearchingValue = window.history.state.serviceSearchingValue || '';
    this.DNSearchingValue = window.history.state.DNSearchingValue || '';
  }

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

  getPendingChanges(pageIndex: number, pageSize: number) {
    this.entityService.getPendingChanges(removeEmpties({
      entity: this.entityNameSearchingValue || null,
      service: this.serviceSearchingValue || null,
      swiftDN: this.DNSearchingValue || null,
      page: pageIndex.toString(),
      size: pageSize.toString()
    }))
      .pipe(take(1)).pipe(data => this.setLoading(data)).subscribe((data: ChangeControlsWithPagination) => {
        this.isLoading = false;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.changeControls = data;
        this.updateTable();
      },
        error => this.isLoading = false
      );
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.changeControls.content);
  }

  getEntityDetails = (entity: Entity) => {
    this.isLoadingDetails = true;
    return this.entityService.getEntityById(entity.entityId).toPromise()
      .finally(() => {
        this.isLoadingDetails = false;
      });
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

  getPendingEntityDetails = (changeControl: ChangeControl) => {
    this.isLoadingDetails = true;
    return this.entityService.getPendingChangeById(changeControl.changeID).toPromise()
      .finally(() => {
        this.isLoadingDetails = false;
      });
  }

  openDetailsDialog(value: Entity | ChangeControl) {
    if (value.changeID && (value as ChangeControl).entityLog.entityId) {
      const getEntityDetails = () => this.getEntityDetails((value as ChangeControl).entityLog);

      getEntityDetails().then((entity: Entity) => entity &&
        this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
          title: `${entity.service}: ${entity.entity}`,
          getTitle: (data: Entity) => `${data.service}: ${data.entity}`,
          getData: getEntityDetails,
          data: entity,
          getTabs: getEntityDetailsTabs,
          displayName: getEntityDisplayName
        })));
    }
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
          this.getPendingChanges(this.pageIndex, this.pageSize);
        }
      }));
  }

  deletePendingChange(changeControl: ChangeControl) {
    const getPendingEntityDetails = () => this.getPendingEntityDetails(changeControl)
      .then((data: ChangeControl) => data && this.addEntityBeforeToChangeControl(data));

    getPendingEntityDetails().then((changeCtrl: ChangeControl) =>
      changeCtrl && this.dialog.open(DeleteDialogComponent, new DetailsDialogConfig({
        title: `Delete ${changeCtrl.changeID}`,
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
          this.getPendingChanges(this.pageIndex, this.pageSize);
        }
      }));
  }

  isTheSameUser = (user) => this.authService.isTheSameUser(user);

  getCurrentRoute = () => this.router.url;

  clearParams = () => {
    this.entityNameSearchingValue = '';
    this.serviceSearchingValue = '';
    this.DNSearchingValue = '';
    this.getPendingChanges(0, this.pageSize);
    return false;
  }

  isClearActive = () =>
    this.entityNameSearchingValue !== '' || this.serviceSearchingValue !== '' || this.DNSearchingValue !== ''

  clearField = (event, field) => {
    this[field] = '';
    this.getPendingChanges(0, this.pageSize);
    event.stopPropagation();
  }
}
