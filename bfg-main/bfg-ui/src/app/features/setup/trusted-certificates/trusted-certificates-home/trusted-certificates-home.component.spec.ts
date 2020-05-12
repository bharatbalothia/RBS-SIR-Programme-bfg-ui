import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TrustedCertificatesHomeComponent } from './trusted-certificates-home.component';

describe('TrustedCertificatesHomeComponent', () => {
  let component: TrustedCertificatesHomeComponent;
  let fixture: ComponentFixture<TrustedCertificatesHomeComponent>;

  beforeEach(async(() => {
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
