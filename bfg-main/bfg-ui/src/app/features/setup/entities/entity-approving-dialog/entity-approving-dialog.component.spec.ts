import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EntityApprovingDialogComponent } from './entity-approving-dialog.component';

describe('EntityApprovingDialogComponent', () => {
  let component: EntityApprovingDialogComponent;
  let fixture: ComponentFixture<EntityApprovingDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EntityApprovingDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EntityApprovingDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
