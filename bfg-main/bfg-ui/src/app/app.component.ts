import { Component } from '@angular/core';
import { MatIconRegistry } from "@angular/material/icon";
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  template: '<router-outlet></router-outlet>'
})
export class AppComponent {
  constructor(
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer
  ) {
    this.matIconRegistry.addSvgIcon(
      'cancel-red-button',
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/icons/cancel-red-button.svg')
    );
    this.matIconRegistry.addSvgIcon(
      'green-circle-with-check',
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/icons/green-circle-with-check.svg')
    );
    this.matIconRegistry.addSvgIcon(
      'yellow-round-error',
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/icons/yellow-round-error.svg')
    );
  }
}
