import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovingDialogComponent } from './approving-dialog.component';

describe('ApprovingDialogComponent', () => {
  let component: ApprovingDialogComponent;
  let fixture: ComponentFixture<ApprovingDialogComponent>;

  beforeEach(async(() => {
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
