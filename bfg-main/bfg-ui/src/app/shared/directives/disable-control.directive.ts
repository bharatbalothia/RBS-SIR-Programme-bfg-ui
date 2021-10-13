import { Directive, Input } from '@angular/core';
import { NgControl } from '@angular/forms';

@Directive({
  selector: '[appDisableControl]'
})
export class DisableControlDirective {

  @Input() set appDisableControl(condition: boolean) {
    if (condition && this.ngControl.control) {
      this.ngControl.control.disable();
    }
  }

  constructor(private ngControl: NgControl) { }

}
