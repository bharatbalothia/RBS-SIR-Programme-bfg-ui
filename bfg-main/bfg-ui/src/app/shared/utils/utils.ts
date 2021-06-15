import { NgxMatDatetimePicker } from '@angular-material-components/datetime-picker';
import { transform, isEqual, isObject } from 'lodash';

export const removeEmpties = (obj) => {
    for (const propName in obj) {
        if (obj[propName] === null || obj[propName] === undefined || obj[propName] === '') {
            delete obj[propName];
        }
    }
    return obj;
};

export const removeNullOrUndefined = (obj) => {
    for (const propName in obj) {
        if (obj[propName] === null || obj[propName] === undefined) {
            delete obj[propName];
        }
    }
    return obj;
};

export const difference = (object, base) => {
    const changes = (object, base): any => {
        return transform(object, (result, value, key) => {
            if (!isEqual(value, base[key])) {
                result[key] = (isObject(value) && isObject(base[key])) ? changes(value, base[key]) : value;
            }
        });
    };
    return changes(object, base);
};

export const capitalizeFirstLetter = (value: string) => value.charAt(0).toUpperCase() + value.slice(1);

export const titleCase = (str: string): string => str ? str[0].toUpperCase() + str.slice(1) : '';

export const setCalendarDblClick = (calendarPicker: NgxMatDatetimePicker<any>) => {
    setTimeout(() => {
        const prevBtn = calendarPicker._popupRef['_pane'].getElementsByClassName('mat-calendar-previous-button')[0];
        const nextBtn = calendarPicker._popupRef['_pane'].getElementsByClassName('mat-calendar-next-button')[0];

        prevBtn.addEventListener('click', () => selectByDblClick(calendarPicker));
        nextBtn.addEventListener('click', () => selectByDblClick(calendarPicker));

        selectByDblClick(calendarPicker);
    }, 0);
};

const selectByDblClick = (calendarPicker: NgxMatDatetimePicker<any>) => {
    setTimeout(() => {
        const elements = calendarPicker._popupRef['_pane'].getElementsByClassName('mat-calendar-body-cell-content');
        Array.from(elements).forEach((element: any) => {
            if (!element.parentElement.getAttribute('aria-disabled')) {
                element.addEventListener('dblclick', () => calendarPicker.ok());
            }
        });
    }, 0);
};

export const defineReferrer = (url) => !document.referrer && Object.defineProperty(document, 'referrer', { get: () => url });

export const removeFirst = (array: any[], toRemove: any): void => {
    const index = array.indexOf(toRemove);
    if (index !== -1) {
        array.splice(index, 1);
    }
};

export const getMappedObjectArray = (obj) => Object.keys(obj).map(key => ({ key, value: obj[key] }));

export const isJson = (str) => {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
}

export const isHtml = (str) => /<\/?[a-z][\s\S]*>/i.test(str);
