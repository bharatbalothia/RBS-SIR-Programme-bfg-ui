import { transform, isEqual, isObject, uniq } from 'lodash';

export const removeEmpties = (obj) => {
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
