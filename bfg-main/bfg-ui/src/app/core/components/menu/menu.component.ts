import { Component, OnInit } from '@angular/core';
import { NestedTreeControl } from '@angular/cdk/tree';
import { MatTreeNestedDataSource } from '@angular/material/tree';
import { MenuNode, MENU_DATA } from './menu-nodes';
import { Router } from '@angular/router';


@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  treeControl = new NestedTreeControl<MenuNode>(node => node.children);
  dataSource = new MatTreeNestedDataSource<MenuNode>();

  constructor(
    private router: Router
  ) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';

    this.dataSource.data = MENU_DATA;
  }

  ngOnInit(): void {
    this.treeControl.expand(this.dataSource.data
      .find(el => this.hasChild(null, el) && el.children.find(child => child.route === this.router.url.split('/')[1])));
  }

  hasChild = (_: number, node: MenuNode) => !!node.children && node.children.length > 0;

}
