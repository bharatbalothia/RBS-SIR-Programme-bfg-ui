import { BrowserModule } from '@angular/platform-browser';
import { NgModule, LOCALE_ID } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { APP_BASE_HREF, registerLocaleData } from '@angular/common';
import localeEnGb from '@angular/common/locales/en-GB';

registerLocaleData(localeEnGb, 'en-GB');
@NgModule({
  declarations: [AppComponent],
  imports: [
    // core
    CoreModule,

    // angular
    BrowserModule,

    // app
    AppRoutingModule,

    BrowserAnimationsModule
  ],
  providers: [
    { provide: LOCALE_ID, useValue: 'en-GB' },
    { provide: APP_BASE_HREF, useValue: '/bfgui' }
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
