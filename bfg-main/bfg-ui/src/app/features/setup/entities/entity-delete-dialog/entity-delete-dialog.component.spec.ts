import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EntityDeleteDialogComponent } from './entity-delete-dialog.component';

describe('EntityDeleteDialogComponent', () => {
  let component: EntityDeleteDialogComponent;
  let fixture: ComponentFixture<EntityDeleteDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EntityDeleteDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EntityDeleteDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
