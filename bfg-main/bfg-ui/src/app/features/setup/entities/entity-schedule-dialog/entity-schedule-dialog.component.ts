import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DetailsDialogData } from 'src/app/shared/components/details-dialog/details-dialog-data.model';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { SCHEDULE_TYPE } from 'src/app/shared/models/schedule/schedule-type';
import { get } from 'lodash';
import { getDisplayName } from '../display-names';
import { SCHEDULE_VALIDATION_MESSAGES } from '../validation-messages';

@Component({
  selector: 'app-entity-schedule-dialog',
  templateUrl: './entity-schedule-dialog.component.html',
  styleUrls: ['./entity-schedule-dialog.component.scss']
})
export class EntityScheduleDialogComponent implements OnInit {

  scheduleType = SCHEDULE_TYPE;
  scheduleValidationMessages = SCHEDULE_VALIDATION_MESSAGES;
  getDisplayName = getDisplayName;

  scheduleFormGroup: FormGroup;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsDialogData,
    private dialog: MatDialogRef<EntityScheduleDialogComponent>,
    private formBuilder: FormBuilder) {
    this.dialog.updateSize('400px');
    this.data.yesCaption = this.data.yesCaption || 'Close';
  }

  ngOnInit(): void {
    this.scheduleFormGroup = this.formBuilder.group({
      isWindow: [true, Validators.required],
      timeStart: ['', Validators.required],
      windowEnd: ['', this.isScheduleTypeWindow() && Validators.required],
      windowInterval: ['', this.isScheduleTypeWindow() && Validators.required],
    });
  }

  getScheduleTypes = () => Object.keys(SCHEDULE_TYPE);

  onScheduleTypeSelect(value) {
    if (value) {
      this.scheduleFormGroup = this.formBuilder.group({
        isWindow: [true, Validators.required],
        timeStart: ['', Validators.required],
        windowEnd: ['', Validators.required],
        windowInterval: ['', Validators.required],
      });
    }
    else {
      this.scheduleFormGroup = this.formBuilder.group({
        isWindow: [false, Validators.required],
        timeStart: ['', Validators.required],
      });
    }
  }

  isScheduleTypeWindow = () => this.scheduleFormGroup && this.scheduleFormGroup.get('isWindow').value;

  submit() {
    console.log(this.scheduleFormGroup);

  }
}
