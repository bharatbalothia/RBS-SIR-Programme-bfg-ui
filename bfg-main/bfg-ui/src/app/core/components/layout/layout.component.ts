import { Component, OnInit } from '@angular/core';
import { BreakpointObserver } from '@angular/cdk/layout';
import { Observable, of } from 'rxjs';
import { filter, map, shareReplay, switchMap } from 'rxjs/operators';
import { AuthService } from '../../auth/auth.service';
import { ApplicationDataService } from 'src/app/shared/models/application-data/application-data.service';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit {

  minWidthForWeb = 900;

  rzMaxWidth = 300;
  rzMinWidth = 10;

  isWeb$: Observable<boolean> = this.breakpointObserver.observe(`(min-width: ${this.minWidthForWeb}px)`)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  shouldHideMenu = false;

  appVersion: string;

  constructor(
    private breakpointObserver: BreakpointObserver,
    private authService: AuthService,
    private applicationDataService: ApplicationDataService,
  ) {

  }

  ngOnInit(): void {
    this.applicationDataService.applicationData.subscribe(data => this.appVersion = data.version);
    this.getHideMenuParam();
  }

  getHideMenuParam = () => {
    const hideMenuParam = new URL(window.location.href).searchParams.get('hideMenu');
    if (hideMenuParam) {
      this.shouldHideMenu = hideMenuParam === 'false' ? false : true;
    }
  }

  logout() {
    this.authService.logOut();
  }

  getUserName() {
    return this.authService.getUserName() || '';
  }

  isAuthenticated() {
    return this.authService.isAuthenticated();
  }
}
