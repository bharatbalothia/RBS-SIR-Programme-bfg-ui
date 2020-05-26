import { Component, OnInit } from '@angular/core';
import { ROUTING_PATHS } from '../../../../core/constants/routing-paths';

@Component({
  selector: 'app-entity-home',
  templateUrl: './entity-home.component.html',
  styleUrls: ['./entity-home.component.scss']
})
export class EntityHomeComponent implements OnInit {

  readonly ROUTING_PATHS = ROUTING_PATHS;

  constructor() { }

  ngOnInit() { }

}
