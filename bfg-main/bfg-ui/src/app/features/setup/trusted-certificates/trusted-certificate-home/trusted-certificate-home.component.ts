import { Component, OnInit } from '@angular/core';
import { ROUTING_PATHS } from 'src/app/core/constants/routing-paths';

@Component({
  selector: 'app-trusted-certificates-home',
  templateUrl: './trusted-certificate-home.component.html',
  styleUrls: ['./trusted-certificate-home.component.scss']
})
export class TrustedCertificateHomeComponent implements OnInit {

  readonly ROUTING_PATHS = ROUTING_PATHS;

  constructor() { }

  ngOnInit(): void {
  }

}
