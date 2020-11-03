import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ErrorMonitorComponent } from './error-monitor.component';

describe('ErrorMonitorComponent', () => {
  let component: ErrorMonitorComponent;
  let fixture: ComponentFixture<ErrorMonitorComponent>;

  beforeEach(async(() => {
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
