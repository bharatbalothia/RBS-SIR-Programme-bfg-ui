import { Component, OnInit } from '@angular/core';
import { NestedTreeControl } from '@angular/cdk/tree';
import { MatTreeNestedDataSource } from '@angular/material/tree';
import { MenuNode, MENU_DATA } from './menu-nodes';
import { Router } from '@angular/router';
import { ApplicationDataService } from 'src/app/shared/models/application-data/application-data.service';
import { AuthService } from '../../auth/auth.service';


@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  treeControl = new NestedTreeControl<MenuNode>(node => node.children);
  dataSource = new MatTreeNestedDataSource<MenuNode>();

  filteredMenuData: MenuNode[];

  constructor(
    private router: Router,
    private applicationDataService: ApplicationDataService,
    private authService: AuthService,
  ) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';

    this.filteredMenuData = this.getMenuByPermissions(MENU_DATA);

    this.applicationDataService.applicationData.subscribe(data =>
      this.dataSource.data = data.sepaDashboardVisibility ?
        this.filteredMenuData : this.filteredMenuData.filter(el => el.name !== 'SEPA Dashboard'));
  }

  ngOnInit(): void {
    this.treeControl.expand(this.dataSource.data
      .find(el => this.hasChild(null, el) && el.children.find(child => child.route === this.router.url.split('/')[1])));
  }

  hasChild = (_: number, node: MenuNode) => !!node.children && node.children.length > 0;

  getMenuByPermissions = (menuData: MenuNode[]) => menuData
    .map(el => this.hasChild(null, el) ?
      { ...el, children: el.children.filter(child => this.authService.isEnoughPermissions(child.permissions)) } : el)
    .filter((el, i) => el.children && el.children.length > 0 ? el : el.route && this.authService.isEnoughPermissions(el.permissions))

}
