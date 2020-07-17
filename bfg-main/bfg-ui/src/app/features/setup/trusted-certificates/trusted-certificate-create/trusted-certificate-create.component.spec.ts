import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TrustedCertificateCreateComponent } from './trusted-certificate-create.component';

describe('TrustedCertificateCreateComponent', () => {
  let component: TrustedCertificateCreateComponent;
  let fixture: ComponentFixture<TrustedCertificateCreateComponent>;

  beforeEach(async(() => {
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
