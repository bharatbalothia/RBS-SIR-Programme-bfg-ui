import { Directive, HostListener } from '@angular/core';
@Directive({
    selector: '[appInputUppercase]'
})
export class InputUppercaseDirective {

    @HostListener('input', ['$event']) onInputChange($event) {
        const targetElement: HTMLInputElement = $event.target;
        const upperCaseValue = targetElement.value.toUpperCase();

        if (targetElement.value !== upperCaseValue) {
            targetElement.value = upperCaseValue;
            targetElement.dispatchEvent(new Event('input'));
        }
    }

}
