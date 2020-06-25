import { Component, OnInit, Input } from '@angular/core';
import { getEntityDisplayName } from 'src/app/features/setup/entities/entity-display-names';
import { Tab } from '../details-dialog/details-dialog-data.model';

@Component({
  selector: 'app-tab-content',
  templateUrl: './tab-content.component.html',
  styleUrls: ['./tab-content.component.scss']
})
export class TabContentComponent implements OnInit {

  getEntityDisplayName = getEntityDisplayName;

  @Input() tab: Tab;

  displayedColumns: string[] = ['fieldName', 'fieldValue'];

  constructor() { }

  ngOnInit(): void {
  }

}
