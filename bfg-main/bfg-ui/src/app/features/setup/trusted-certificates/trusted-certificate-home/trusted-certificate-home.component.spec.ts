import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { TrustedCertificatesHomeComponent } from './trusted-certificate-home.component';

describe('TrustedCertificatesHomeComponent', () => {
  let component: TrustedCertificatesHomeComponent;
  let fixture: ComponentFixture<TrustedCertificatesHomeComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ TrustedCertificatesHomeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TrustedCertificatesHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
