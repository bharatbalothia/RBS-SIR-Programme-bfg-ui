import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TrustedCertificatePendingComponent } from './trusted-certificate-pending.component';

describe('TrustedCertificatePendingComponent', () => {
  let component: TrustedCertificatePendingComponent;
  let fixture: ComponentFixture<TrustedCertificatePendingComponent>;

  beforeEach(async(() => {
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
