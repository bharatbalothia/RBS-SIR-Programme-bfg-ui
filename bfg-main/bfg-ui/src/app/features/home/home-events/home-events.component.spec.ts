import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { HomeEventsComponent } from './home-events.component';

describe('HomeEventsComponent', () => {
  let component: HomeEventsComponent;
  let fixture: ComponentFixture<HomeEventsComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ HomeEventsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeEventsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
