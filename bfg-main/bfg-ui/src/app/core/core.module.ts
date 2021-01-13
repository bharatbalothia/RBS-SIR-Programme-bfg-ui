import { NgModule } from '@angular/core';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { LayoutComponent } from './components/layout/layout.component';
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
    PageNotFoundComponent,
    MenuComponent,
    LoginComponent,
  ],
  imports: [
    RouterModule,
    SharedModule
  ],
  exports: [
    LayoutComponent,
    LoginComponent,
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
