import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BusinessProcessDialogComponent } from './business-process-dialog.component';

describe('BusinessProcessDialogComponent', () => {
  let component: BusinessProcessDialogComponent;
  let fixture: ComponentFixture<BusinessProcessDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BusinessProcessDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BusinessProcessDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
