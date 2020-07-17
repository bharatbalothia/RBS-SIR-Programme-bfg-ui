import { TestBed } from '@angular/core/testing';

import { TrustedCertificateServiceService } from './trusted-certificate-service.service';

describe('TrustedCertificateServiceService', () => {
  let service: TrustedCertificateServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrustedCertificateServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
