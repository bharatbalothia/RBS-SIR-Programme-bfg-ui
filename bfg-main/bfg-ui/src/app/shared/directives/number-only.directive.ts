import { Directive, Input, HostListener, forwardRef } from '@angular/core';

@Directive({
  selector: '[appNumberOnly]'
})
export class NumberOnlyDirective {

  @Input() min: number;
  @Input() max: number;

  @HostListener('keydown', ['$event'])
  public onKeydown(event: KeyboardEvent) {
    const { key } = event;
    if (this.isSpecialOperation(event) || !this.isKeyPrintable(event)) {
      return true;
    }
    const newValue = this.getNewValue(event.target as HTMLInputElement, key);
    if (!this.valueIsValid(newValue)) {
      return false;
    }
  }

  @HostListener('paste', ['$event'])
  public onPaste(event: ClipboardEvent) {
    const pastedText = event.clipboardData.getData('text');
    const newValue = this.getNewValue(event.target as HTMLInputElement, pastedText);
    if (!this.valueIsValid(newValue)) {
      return false;
    }
  }

  private getNewValue(target: HTMLInputElement, str: string): string {
    const { value = '', selectionStart, selectionEnd } = target;
    return [
      ...value.split('').splice(0, selectionStart),
      str,
      ...value.split('').splice(selectionEnd)].join('');
  }

  private valueIsValid(value: string): boolean {
    if (/^-?\d*(,|\.)?\d*$/.test(value) && parseInt(value) >= this.min && parseInt(value) <= this.max) {
      return true;
    }
    return false;
  }

  private isSpecialOperation(event: KeyboardEvent): boolean {
    const { keyCode, ctrlKey, metaKey } = event;
    // allow ctr-A/C/V/X/Y/Z
    const keysACVXYZ = [65, 67, 86, 88, 89, 90];
    if ((ctrlKey || metaKey) && keysACVXYZ.indexOf(keyCode) >= 0) {
      return true;
    }
    return false;
  }

  private isKeyPrintable(event: KeyboardEvent): boolean {
    const { keyCode } = event;
    return (
      (keyCode > 47 && keyCode < 58) || // number keys
      keyCode === 32 || keyCode === 13 || // spacebar & return key(s)
      (keyCode > 64 && keyCode < 91) || // letter keys
      (keyCode > 95 && keyCode < 112) || // numpad keys
      (keyCode > 185 && keyCode < 193) || // ;=,-./` (in order)
      (keyCode > 218 && keyCode < 223));      // [\]' (in order)
  }
}