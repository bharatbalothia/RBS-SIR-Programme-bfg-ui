import { Component } from '@angular/core';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';
import { ApplicationDataService } from 'src/app/shared/models/application-data/application-data.service';

@Component({
  selector: 'app-root',
  template: '<app-layout></app-layout>'
})
export class AppComponent {
  constructor(
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer,
    private applicationDataService: ApplicationDataService
  ) {
    this.applicationDataService.getApplicationVersion();

    this.matIconRegistry.addSvgIcon(
      'cancel-red-button',
      this.domSanitizer.bypassSecurityTrustResourceUrl('./assets/icons/cancel-red-button.svg')
    );
    this.matIconRegistry.addSvgIcon(
      'green-circle-with-check',
      this.domSanitizer.bypassSecurityTrustResourceUrl('./assets/icons/green-circle-with-check.svg')
    );
    this.matIconRegistry.addSvgIcon(
      'yellow-round-error',
      this.domSanitizer.bypassSecurityTrustResourceUrl('./assets/icons/yellow-round-error.svg')
    );
  }
}
