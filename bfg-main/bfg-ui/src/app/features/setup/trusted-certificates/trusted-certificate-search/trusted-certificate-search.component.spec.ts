import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TrustedCertificateSearchComponent } from './trusted-certificate-search.component';

describe('TrustedCertificateSearchComponent', () => {
  let component: TrustedCertificateSearchComponent;
  let fixture: ComponentFixture<TrustedCertificateSearchComponent>;

  beforeEach(async(() => {
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
