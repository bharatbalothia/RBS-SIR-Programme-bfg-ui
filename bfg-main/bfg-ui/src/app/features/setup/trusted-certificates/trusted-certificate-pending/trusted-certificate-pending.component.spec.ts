import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { TrustedCertificatePendingComponent } from './trusted-certificate-pending.component';

describe('TrustedCertificatePendingComponent', () => {
  let component: TrustedCertificatePendingComponent;
  let fixture: ComponentFixture<TrustedCertificatePendingComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ TrustedCertificatePendingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TrustedCertificatePendingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
