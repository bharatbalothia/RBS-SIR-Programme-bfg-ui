import { Entity } from './entity.model';

export interface EntitiesWithPagination {
    content: [
        Entity
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
