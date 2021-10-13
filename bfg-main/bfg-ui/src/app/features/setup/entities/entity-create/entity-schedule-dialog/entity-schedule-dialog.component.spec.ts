import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { EntityScheduleDialogComponent } from './entity-schedule-dialog.component';

describe('EntityScheduleDialogComponent', () => {
  let component: EntityScheduleDialogComponent;
  let fixture: ComponentFixture<EntityScheduleDialogComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ EntityScheduleDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EntityScheduleDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
