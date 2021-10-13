import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { DisplayTableCellComponent } from './display-table-cell.component';

describe('DisplayTableCellComponent', () => {
  let component: DisplayTableCellComponent;
  let fixture: ComponentFixture<DisplayTableCellComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ DisplayTableCellComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DisplayTableCellComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
