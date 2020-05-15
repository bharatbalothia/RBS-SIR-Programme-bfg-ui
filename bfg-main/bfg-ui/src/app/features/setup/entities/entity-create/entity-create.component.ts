import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { INBOUND_REQUEST_TYPES } from '../inbound-request-types';
import { ENTITY_VALIDATION_MESSAGES } from '../entity-validation-messages';
@Component({
  selector: 'app-entity-create',
  templateUrl: './entity-create.component.html',
  styleUrls: ['./entity-create.component.scss']
})
export class EntityCreateComponent implements OnInit {

  inboundRequestTypeList: string[] = INBOUND_REQUEST_TYPES;
  validationMessages = ENTITY_VALIDATION_MESSAGES;

  entityTypeFormGroup: FormGroup;
  entityPageFormGroup: FormGroup;
  SWIFTDetailsFormGroup: FormGroup;
  summaryPageFormGroup: FormGroup;

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.entityTypeFormGroup = this.formBuilder.group({
      entityTypeSelect: ['', Validators.required]
    });
    this.entityPageFormGroup = this.formBuilder.group({
      entity: ['', [
        Validators.required,
        Validators.pattern('[A-Z]{6,6}[A-Z2-9][A-NP-Z0-9]([A-Z0-9]{3,3}){0,1}')
      ]],
      routeInbound: [true, Validators.required],
      inboundRequestorDN: ['', [
        Validators.required,
        Validators.pattern(/^ou=(.*?),o=([A-Z]{6,6}[A-Z2-9][A-NP-Z0-9]([A-Z0-9]{3,3}){0,1}),o=swift$/)
      ]],
      inboundResponderDN: ['', Validators.required],
      inboundService: ['', Validators.required],
      inboundRequestType: [[], Validators.nullValidator],
      inboundDirectory: [true, Validators.required],
      inboundRoutingRule: [true, Validators.required]
    });
    this.SWIFTDetailsFormGroup = this.formBuilder.group({
      requestorDN: ['', Validators.required],
      responderDN: ['', Validators.required],
      requestType: ['', Validators.nullValidator],
      trace: [null, Validators.nullValidator],
      snF: [null, Validators.nullValidator],
      deliveryNotification: ['', Validators.nullValidator],
      nonRepudiation: ['', Validators.nullValidator],
      endToEndSigning: ['None', Validators.required],
      deliveryNotificationDN: ['', Validators.nullValidator],
      deliveryNotificationRT: ['', Validators.nullValidator],
      requestReference: ['', Validators.nullValidator],
      fileInfo: ['', Validators.nullValidator],
      fileDescription: ['', Validators.nullValidator],
      transferInfo: ['', Validators.nullValidator],
      transferDescription: ['', Validators.nullValidator]
    });
    this.summaryPageFormGroup = this.formBuilder.group({
      changerComments: ['', Validators.nullValidator]
    });
  }

  onInboundRequestTypeRemoved(inboundRequestType: string) {
    const inboundRequestTypeList = this.entityPageFormGroup.get('inboundRequestType').value as string[];
    this.removeFirst(inboundRequestTypeList, inboundRequestType);
    this.entityPageFormGroup.get('inboundRequestType').setValue(inboundRequestTypeList);
  }

  private removeFirst<T>(array: T[], toRemove: T): void {
    const index = array.indexOf(toRemove);
    if (index !== -1) {
      array.splice(index, 1);
    }
  }

}
