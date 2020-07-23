import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TrustedCertificateApprovingDialogComponent } from './trusted-certificate-approving-dialog.component';

describe('TrustedCertificateApprovingDialogComponent', () => {
  let component: TrustedCertificateApprovingDialogComponent;
  let fixture: ComponentFixture<TrustedCertificateApprovingDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TrustedCertificateApprovingDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TrustedCertificateApprovingDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
