import { Component, OnInit, OnDestroy } from '@angular/core';
import { Entity } from 'src/app/shared/entity/entity.model';
import { EntityService } from 'src/app/shared/entity/entity.service';
import { MatTableDataSource } from '@angular/material/table';
import { take } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { DetailsDialogComponent } from 'src/app/shared/components/details-dialog/details-dialog.component';
import { DetailsDialogConfig } from 'src/app/shared/components/details-dialog/details-dialog-config.model';
import { ENTITY_DISPLAY_NAMES, getEntityDisplayName, getEntityDetailsFields } from '../entity-display-names';
import { EntityApprovingDialogComponent } from '../entity-approving-dialog/entity-approving-dialog.component';
import { Section } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { ChangeControl } from 'src/app/shared/entity/change-control.model';

@Component({
  selector: 'app-entity-pending',
  templateUrl: './entity-pending.component.html',
  styleUrls: ['./entity-pending.component.scss']
})
export class EntityPendingComponent implements OnInit {

  entityDisplayNames = ENTITY_DISPLAY_NAMES;

  isLoading = true;
  changeControls: ChangeControl[] = [];
  displayedColumns: string[] = ['action', 'changes', 'entity', 'service'];
  dataSource: MatTableDataSource<ChangeControl>;

  constructor(
    private entityService: EntityService,
    private dialog: MatDialog
  ) { }

  ngOnInit() {
    this.entityService.getPendingChanges().pipe(take(1)).subscribe((data: ChangeControl[]) => {
      this.isLoading = false;
      this.changeControls = data;
      this.updateTable();
    });
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.changeControls);
  }

  getPendingChangesDialogInfo(changeControl: ChangeControl): Section[] {
    return [
      {
        sectionTitle: 'Change Details',
        sectionItems: [
          { fieldName: 'Change ID', fieldValue: changeControl.changeID },
          { fieldName: 'Object type', fieldValue: changeControl.objectType },
          { fieldName: 'Operation', fieldValue: changeControl.operation },
          { fieldName: 'Status', fieldValue: changeControl.status },
          { fieldName: 'Changer', fieldValue: changeControl.changer },
          { fieldName: 'Date Changed', fieldValue: changeControl.dateChanged },
          { fieldName: 'Changer Notes', fieldValue: changeControl.changerComments },
          { fieldName: 'Approver', fieldValue: changeControl.approver },
          { fieldName: 'Approver Notes', fieldValue: changeControl.approverComments },
        ]
      }
    ];
  }

  openInfoDialog(changeControl: ChangeControl) {
    this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
      title: `Change Record: Pending`,
      sections: this.getPendingChangesDialogInfo(changeControl),
    }));
  }

  openEntityDetailsDialog(entity: Entity) {
    this.dialog.open(DetailsDialogComponent, new DetailsDialogConfig({
      title: `${entity.service}: ${entity.entity}`,
      sections: getEntityDetailsFields(entity),
    }));
  }

  openApprovingDialog(changeControl: ChangeControl) {
    this.dialog.open(EntityApprovingDialogComponent, new DetailsDialogConfig({
      title: 'Approve Change',
      sections: this.getPendingChangesDialogInfo(changeControl)
    }));
  }

}
