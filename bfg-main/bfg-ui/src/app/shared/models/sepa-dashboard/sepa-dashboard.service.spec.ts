import { TestBed } from '@angular/core/testing';

import { SEPADashboardService } from './sepa-dashboard.service';

describe('SepaDashboardService', () => {
  let service: SEPADashboardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SEPADashboardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
