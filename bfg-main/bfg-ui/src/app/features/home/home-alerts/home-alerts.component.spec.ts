import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { HomeAlertsComponent } from './home-alerts.component';

describe('HomeAlertsComponent', () => {
  let component: HomeAlertsComponent;
  let fixture: ComponentFixture<HomeAlertsComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ HomeAlertsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeAlertsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
