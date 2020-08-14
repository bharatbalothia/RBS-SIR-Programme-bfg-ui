import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DragableDialogWrapperComponent } from './dragable-dialog-wrapper.component';

describe('DragableDialogWrapperComponent', () => {
  let component: DragableDialogWrapperComponent;
  let fixture: ComponentFixture<DragableDialogWrapperComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DragableDialogWrapperComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DragableDialogWrapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
