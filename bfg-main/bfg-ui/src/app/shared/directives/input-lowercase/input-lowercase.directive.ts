import { Directive, EventEmitter, HostListener, Output } from '@angular/core';
@Directive({
  selector: '[appInputLowercase]'
})
export class InputLowercaseDirective {

  @HostListener('input', ['$event']) onInputChange($event) {
    const targetElement: HTMLInputElement = $event.target;
    targetElement.value = targetElement.value.toLowerCase();
    targetElement.dispatchEvent(new Event('input'));
  }

}
