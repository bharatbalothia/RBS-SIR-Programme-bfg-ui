import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TransactionsWithValueReportsDialogComponent } from './transactions-with-value-reports-dialog.component';

describe('TransactionsWithValueReportsDialogComponent', () => {
  let component: TransactionsWithValueReportsDialogComponent;
  let fixture: ComponentFixture<TransactionsWithValueReportsDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TransactionsWithValueReportsDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TransactionsWithValueReportsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
