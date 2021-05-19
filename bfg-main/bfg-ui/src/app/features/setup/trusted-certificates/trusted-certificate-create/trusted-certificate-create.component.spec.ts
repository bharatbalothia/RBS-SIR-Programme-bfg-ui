import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { TrustedCertificateCreateComponent } from './trusted-certificate-create.component';

describe('TrustedCertificateCreateComponent', () => {
  let component: TrustedCertificateCreateComponent;
  let fixture: ComponentFixture<TrustedCertificateCreateComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ TrustedCertificateCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TrustedCertificateCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
