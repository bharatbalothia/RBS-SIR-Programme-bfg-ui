import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { FileMonitorComponent } from './file-monitor.component';

describe('FileMonitorComponent', () => {
  let component: FileMonitorComponent;
  let fixture: ComponentFixture<FileMonitorComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ FileMonitorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FileMonitorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
