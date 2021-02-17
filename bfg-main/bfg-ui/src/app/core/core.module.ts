import { NgModule } from '@angular/core';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { LayoutComponent } from './components/layout/layout.component';
import { ChildLayoutComponent } from './components/child-layout/child-layout.component';
import { SessionExpirationComponent } from './components/session-expiration/session-expiration.component';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { MenuComponent } from './components/menu/menu.component';
import { LoginComponent } from './components/login/login.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './auth/auth.interceptor';
import { TokenInterceptor } from './auth/token.interceptor';

@NgModule({
  declarations: [
    LayoutComponent,
    ChildLayoutComponent,
    PageNotFoundComponent,
    MenuComponent,
    LoginComponent,
    SessionExpirationComponent
  ],
  imports: [
    RouterModule,
    SharedModule
  ],
  exports: [
    LayoutComponent,
    ChildLayoutComponent,
    LoginComponent,
    SessionExpirationComponent
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
  ]
})
export class CoreModule { }
