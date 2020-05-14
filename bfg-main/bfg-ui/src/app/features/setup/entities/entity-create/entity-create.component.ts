import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { INBOUND_REQUEST_TYPES } from '../inbound-request-types';
@Component({
  selector: 'app-entity-create',
  templateUrl: './entity-create.component.html',
  styleUrls: ['./entity-create.component.scss']
})
export class EntityCreateComponent implements OnInit {

  inboundRequestTypeList: string[] = INBOUND_REQUEST_TYPES;

  entityTypeFormGroup: FormGroup;
  entityPageFormGroup: FormGroup;
  SWIFTDetailsFormGroup: FormGroup;
  summaryPageFormGroup: FormGroup;

  constructor(private _formBuilder: FormBuilder) { }

  ngOnInit() {
    this.entityTypeFormGroup = this._formBuilder.group({
      entityTypeSelect: ['', Validators.required]
    });
    this.entityPageFormGroup = this._formBuilder.group({
      entity: ['', Validators.required],
      routeInbound: [true, Validators.required],
      inboundRequestorDN: ['', Validators.required],
      inboundResponderDN: ['', Validators.required],
      inboundService: ['', Validators.required],
      inboundRequestType: [[], Validators.nullValidator],
      inboundDirectory: [true, Validators.required],
      inboundRoutingRule: [true, Validators.required]
    });
    this.SWIFTDetailsFormGroup = this._formBuilder.group({
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
    this.summaryPageFormGroup = this._formBuilder.group({
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
