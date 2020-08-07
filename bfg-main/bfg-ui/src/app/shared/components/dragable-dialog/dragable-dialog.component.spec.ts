import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DragableDialogComponent } from './dragable-dialog.component';

describe('DragableDialogComponent', () => {
  let component: DragableDialogComponent;
  let fixture: ComponentFixture<DragableDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DragableDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DragableDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
