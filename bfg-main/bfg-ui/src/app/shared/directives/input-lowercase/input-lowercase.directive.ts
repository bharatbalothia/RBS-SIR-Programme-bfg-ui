import { Directive, HostListener } from '@angular/core';
@Directive({
  selector: '[appInputLowercase]'
})
export class InputLowercaseDirective {

  @HostListener('input', ['$event']) onInputChange($event) {
    const targetElement: HTMLInputElement = $event.target;
    const lowerCaseValue = targetElement.value.toLowerCase();

    if (targetElement.value !== lowerCaseValue) {
      targetElement.value = lowerCaseValue;
      targetElement.dispatchEvent(new Event('input'));
    }
  }

}
