import { Component, OnInit, Input } from '@angular/core';
import { getDisplayName } from 'src/app/features/setup/entities/display-names';
import { Tab } from '../details-dialog/details-dialog-data.model';

@Component({
  selector: 'app-tab-content',
  templateUrl: './tab-content.component.html',
  styleUrls: ['./tab-content.component.scss']
})
export class TabContentComponent implements OnInit {

  getDisplayName = getDisplayName;

  @Input() tab: Tab;

  displayedColumns: string[] = ['fieldName', 'fieldValue'];

  constructor() { }

  ngOnInit(): void {
  }

}
