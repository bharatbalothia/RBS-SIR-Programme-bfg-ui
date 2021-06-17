import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { get, isUndefined } from 'lodash';
import { NON_NEGATIVE_INT, TIME_24 } from 'src/app/core/constants/validation-regexes';
import { TooltipService } from 'src/app/shared/components/tooltip/tooltip.service';
import { SCHEDULE_TYPE } from 'src/app/shared/models/schedule/schedule-type';
import { Schedule } from 'src/app/shared/models/schedule/schedule.model';
import { getEntityDisplayName } from '../entity-display-names';
import { SCHEDULE_VALIDATION_MESSAGES } from '../validation-messages';

@Component({
  selector: 'app-entity-schedule-dialog',
  templateUrl: './entity-schedule-dialog.component.html',
  styleUrls: ['./entity-schedule-dialog.component.scss']
})
export class EntityScheduleDialogComponent implements OnInit {

  scheduleType = SCHEDULE_TYPE;
  scheduleValidationMessages = SCHEDULE_VALIDATION_MESSAGES;
  getEntityDisplayName = getEntityDisplayName;

  scheduleFormGroup: FormGroup;

  editSchedule: Schedule;

  fileTypes: string[];

  constructor(
    @Inject(MAT_DIALOG_DATA) public data,
    private dialog: MatDialogRef<EntityScheduleDialogComponent>,
    private formBuilder: FormBuilder,
    private toolTip: TooltipService
  ) {
    this.data.yesCaption = this.data.yesCaption || 'Close';

    this.editSchedule = get(this.data, 'actionData.editSchedule');
    this.fileTypes = get(this.data, 'actionData.fileTypes', []);
  }

  ngOnInit(): void {
    if (this.isEditStatus()) {
      this.onScheduleTypeSelect(this.editSchedule.isWindow, this.editSchedule);
    }
    else {
      this.onScheduleTypeSelect(true);
    }
  }

  isEditStatus = () => !isUndefined(this.editSchedule);

  getScheduleTypes = () => Object.keys(SCHEDULE_TYPE);

  onScheduleTypeSelect(value: boolean, defaultValue?: Schedule) {
    if (value) {
      this.scheduleFormGroup = this.formBuilder.group({
        isWindow: [true, Validators.required],
        timeStart: [get(defaultValue, 'timeStart') || '', {
          validators: [
            Validators.required,
            Validators.pattern(TIME_24)
          ]
        }],
        windowEnd: [get(defaultValue, 'windowEnd') || '', {
          validators: [
            Validators.required,
            Validators.pattern(TIME_24)
          ]
        }],
        windowInterval: [get(defaultValue, 'windowInterval') || '', {
          validators: [
            Validators.required,
            Validators.pattern(NON_NEGATIVE_INT)
          ]
        }],
        fileType: [get(defaultValue, 'fileType') || this.fileTypes[0], Validators.required],
      });
    }
    else {
      this.scheduleFormGroup = this.formBuilder.group({
        isWindow: [false, Validators.required],
        timeStart: [get(defaultValue, 'timeStart') || '', {
          validators: [
            Validators.required,
            Validators.pattern(TIME_24)
          ]
        }],
        fileType: [get(defaultValue, 'fileType') || this.fileTypes[0], Validators.required],
      });
    }
  }

  ifFieldHasError = (field: string, error: string) =>
    this.scheduleFormGroup.get(field) && this.scheduleFormGroup.get(field).hasError(error)

  isScheduleTypeWindow = () => this.scheduleFormGroup && this.scheduleFormGroup.get('isWindow').value;

  submit() {
    if (this.isEditStatus()) {
      this.dialog.close({ editedSchedule: this.scheduleFormGroup.value });
    }
    else {
      this.dialog.close({ newSchedule: this.scheduleFormGroup.value });
    }
  }

  getTooltip(field: string): string {
    const toolTip = this.toolTip.getTooltip({
      type: 'entity',
      qualifier: 'sct-schedule',
      mode: this.isEditStatus() ? 'edit' : 'create',
      fieldName: field
    });
    return toolTip.length > 0 ? toolTip : (this.getEntityDisplayName(field) || '');
  }
}
