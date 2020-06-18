import { Entity } from './entity.model';
import { ChangeControl } from '../changeControl/change-control.model';

export interface EntitiesWithPagination {
    content: [
        Entity | ChangeControl
    ];
    pageable: {
        pageSize: number;
        pageNumber: number;
        offset: number;
    };
    size: number;
    totalElements: number;
    totalPages: number;

}
