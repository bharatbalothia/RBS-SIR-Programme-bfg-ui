import { Component, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { AuthService } from '../../auth/auth.service';
import { ApplicationDataService } from 'src/app/shared/models/application-data/application-data.service';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit {

  minWidthForWeb = 900;

  isWeb$: Observable<boolean> = this.breakpointObserver.observe(`(min-width: ${this.minWidthForWeb}px)`)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  appVersion: string;

  constructor(
    private breakpointObserver: BreakpointObserver,
    private authService: AuthService,
    private applicationDataService: ApplicationDataService
  ) { }

  ngOnInit(): void {
    this.applicationDataService.applicationData.subscribe(data => this.appVersion = data.version);
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
