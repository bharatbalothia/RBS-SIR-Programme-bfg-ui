import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { ApprovingDialogComponent } from './approving-dialog.component';

describe('ApprovingDialogComponent', () => {
  let component: ApprovingDialogComponent;
  let fixture: ComponentFixture<ApprovingDialogComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ApprovingDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovingDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
