import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { ErrorMonitorComponent } from './error-monitor.component';

describe('ErrorMonitorComponent', () => {
  let component: ErrorMonitorComponent;
  let fixture: ComponentFixture<ErrorMonitorComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ErrorMonitorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ErrorMonitorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
