import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { TransactionsDialogComponent } from './transactions-dialog.component';

describe('TransactionsDialogComponent', () => {
  let component: TransactionsDialogComponent;
  let fixture: ComponentFixture<TransactionsDialogComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ TransactionsDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TransactionsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
