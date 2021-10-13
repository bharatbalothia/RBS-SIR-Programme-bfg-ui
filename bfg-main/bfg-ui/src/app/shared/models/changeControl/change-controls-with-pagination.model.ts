import { ChangeControl } from './change-control.model';

export interface ChangeControlsWithPagination {
    content: [
        ChangeControl
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
