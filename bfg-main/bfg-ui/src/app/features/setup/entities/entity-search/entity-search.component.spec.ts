import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { EntitySearchComponent } from './entity-search.component';

describe('EntitySearchComponent', () => {
  let component: EntitySearchComponent;
  let fixture: ComponentFixture<EntitySearchComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ EntitySearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EntitySearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
