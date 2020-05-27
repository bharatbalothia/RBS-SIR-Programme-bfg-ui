import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EntityHomeComponent } from './entity-home.component';

describe('EntityHomeComponent', () => {
  let component: EntityHomeComponent;
  let fixture: ComponentFixture<EntityHomeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EntityHomeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EntityHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
