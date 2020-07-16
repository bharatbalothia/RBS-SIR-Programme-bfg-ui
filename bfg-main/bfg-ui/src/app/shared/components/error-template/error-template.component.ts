import { Component, OnInit, Input } from '@angular/core';
import { ErrorMessage, getErrorsMessage } from 'src/app/core/utils/error-template';

@Component({
  selector: 'app-error-template',
  templateUrl: './error-template.component.html',
  styleUrls: ['./error-template.component.scss']
})
export class ErrorTemplateComponent implements OnInit {

  getErrorsMessage = getErrorsMessage;

  @Input() errorMessage: ErrorMessage;

  constructor() { }

  ngOnInit(): void {
  }

}
