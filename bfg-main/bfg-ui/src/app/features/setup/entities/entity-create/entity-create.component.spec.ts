import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { EntityCreateComponent } from './entity-create.component';

describe('EntityCreateComponent', () => {
  let component: EntityCreateComponent;
  let fixture: ComponentFixture<EntityCreateComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ EntityCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EntityCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
