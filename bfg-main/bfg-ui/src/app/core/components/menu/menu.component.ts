import { Component, OnInit } from '@angular/core';
import { NestedTreeControl } from '@angular/cdk/tree';
import { MatTreeNestedDataSource } from '@angular/material/tree';
import { MenuNode, MENU_DATA } from './menu-nodes';


@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  treeControl = new NestedTreeControl<MenuNode>(node => node.children);
  dataSource = new MatTreeNestedDataSource<MenuNode>();

  constructor() {
    this.dataSource.data = MENU_DATA;
   }

  ngOnInit(): void {
  }

  hasChild = (_: number, node: MenuNode) => !!node.children && node.children.length > 0;

}
