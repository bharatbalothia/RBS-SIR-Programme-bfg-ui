import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TransactionsWithValueComponent } from './transactions-with-value.component';

describe('TransactionsWithValueComponent', () => {
  let component: TransactionsWithValueComponent;
  let fixture: ComponentFixture<TransactionsWithValueComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TransactionsWithValueComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TransactionsWithValueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
