import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { TablePaginatorComponent } from './table-paginator.component';

describe('TablePaginatorComponent', () => {
  let component: TablePaginatorComponent;
  let fixture: ComponentFixture<TablePaginatorComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ TablePaginatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TablePaginatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
