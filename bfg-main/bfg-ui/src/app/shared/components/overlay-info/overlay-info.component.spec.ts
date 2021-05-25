import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { OverlayInfoComponent } from './overlay-info.component';

describe('OverlayInfoComponent', () => {
  let component: OverlayInfoComponent;
  let fixture: ComponentFixture<OverlayInfoComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ OverlayInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OverlayInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
