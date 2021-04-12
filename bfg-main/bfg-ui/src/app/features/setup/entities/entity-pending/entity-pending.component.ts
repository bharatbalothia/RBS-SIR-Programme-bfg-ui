import { Component, OnInit } from '@angular/core';
import { Entity } from 'src/app/shared/models/entity/entity.model';
import { EntityService } from 'src/app/shared/models/entity/entity.service';
import { MatTableDataSource } from '@angular/material/table';
import { take } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';
import { ENTITY_DISPLAY_NAMES, getEntityDetailsTabs, getPendingChangesTabs, getEntityDisplayName } from '../entity-display-names';
import { ChangeControl } from 'src/app/shared/models/changeControl/change-control.model';
import { get, isEmpty } from 'lodash';
import { ChangeControlsWithPagination } from 'src/app/shared/models/changeControl/change-controls-with-pagination.model';
import { ApprovingDialogComponent } from 'src/app/shared/components/approving-dialog/approving-dialog.component';
import { ERROR_MESSAGES } from 'src/app/core/constants/error-messages';
import { AuthService } from 'src/app/core/auth/auth.service';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';
import { Router } from '@angular/router';
import { CHANGE_OPERATION } from 'src/app/shared/models/changeControl/change-operation';
import { DeleteDialogComponent } from 'src/app/shared/components/delete-dialog/delete-dialog.component';
import { NotificationService } from 'src/app/shared/services/notification.service';
import { removeEmpties } from 'src/app/shared/utils/utils';

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
  displayedColumns: string[] = ['action', 'changes', 'entity', 'service'];
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
      .then((data: Entity) => {
        this.isLoadingDetails = false;
        return data;
      }).finally(() => {
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
    return this.entityService.getPendingEntityById(changeControl.changeID).toPromise()
      .then((data: Entity) => {
        this.isLoadingDetails = false;
        return ({ ...changeControl, entityLog: data });
      }).finally(() => {
        this.isLoadingDetails = false;
      });
  }

  openInfoDialog(changeControl: ChangeControl) {
    const getChangeControl = () => this.getPendingEntityDetails(changeControl)
      .then((data: ChangeControl) => data && this.addEntityBeforeToChangeControl(data));

    getChangeControl().then((changeCtrl: ChangeControl) => changeCtrl &&
      this.dialog.open(ApprovingDialogComponent, new DetailsDialogConfig({
        title: `Change Record: Pending`,
        data: changeCtrl,
        getData: getChangeControl,
        getTabs: getPendingChangesTabs,
        displayName: getEntityDisplayName
      })));
  }

  openDetailsDialog(value: Entity | ChangeControl) {
    if (value.changeID && (value as ChangeControl).entityLog.entityId) {
      const getEntityDetails = () => this.getEntityDetails((value as ChangeControl).entityLog);

      getEntityDetails().then((entity: Entity) => entity &&
        this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
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
    if (this.entityNameSearchingValue !== '' || this.serviceSearchingValue !== '' || this.DNSearchingValue !== '') {
      this.entityNameSearchingValue = '';
      this.serviceSearchingValue = '';
      this.DNSearchingValue = '';
      this.getPendingChanges(0, this.pageSize);
    }
    return false;
  }
}
