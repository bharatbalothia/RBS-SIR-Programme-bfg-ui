import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { TransactionSearchComponent } from './transaction-search.component';

describe('TransactionSearchComponent', () => {
  let component: TransactionSearchComponent;
  let fixture: ComponentFixture<TransactionSearchComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ TransactionSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TransactionSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
