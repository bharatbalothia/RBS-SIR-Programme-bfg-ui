import { Directive, EventEmitter, HostListener, Output } from '@angular/core';
@Directive({
  selector: '[appInputLowercase]'
})
export class InputLowercaseDirective {

  @HostListener('input', ['$event']) onInputChange($event) {
    $event.target.value = $event.target.value.toLowerCase();
  }

}
