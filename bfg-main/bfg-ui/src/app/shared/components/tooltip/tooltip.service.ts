import { Injectable } from '@angular/core';
import { TOOLTIP_CONTENT } from './tooltip.contetnt';
import { TooltipKey } from './tooltip.model';

@Injectable({
  providedIn: 'root'
})
export class TooltipService {

  constructor() { }

  getTooltip( toolTipKey: TooltipKey): string{
    const key: string =
      (toolTipKey.type || '') + '.' +
      (toolTipKey.qualifier || '') + '.' +
      (toolTipKey.fieldName || '') + '.' +
      (toolTipKey.mode || '');

    return TOOLTIP_CONTENT[key] || '';
  }
}
