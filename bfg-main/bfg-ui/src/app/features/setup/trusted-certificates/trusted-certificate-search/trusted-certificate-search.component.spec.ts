import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { TrustedCertificateSearchComponent } from './trusted-certificate-search.component';

describe('TrustedCertificateSearchComponent', () => {
  let component: TrustedCertificateSearchComponent;
  let fixture: ComponentFixture<TrustedCertificateSearchComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ TrustedCertificateSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TrustedCertificateSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
