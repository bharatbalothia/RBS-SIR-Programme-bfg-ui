import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-entity-create',
  templateUrl: './entity-create.component.html',
  styleUrls: ['./entity-create.component.scss']
})
export class EntityCreateComponent implements OnInit {

  entityTypeFormGroup: FormGroup;
  entityPageFormGroup: FormGroup;
  SWIFTDetailsFormGroup: FormGroup;
  summaryPageFormGroup: FormGroup;

  constructor(private _formBuilder: FormBuilder) {}

  ngOnInit() {
    this.entityTypeFormGroup = this._formBuilder.group({
      entityTypeSelect: ['', Validators.required]
    });
    this.entityPageFormGroup = this._formBuilder.group({
      entity: ['', Validators.required],
      routeInbound: ['', Validators.required]
    });
    this.SWIFTDetailsFormGroup = this._formBuilder.group({
      
    });
    this.summaryPageFormGroup = this._formBuilder.group({

    });
  }

}
