import { File } from './file.model';

export interface FilesWithPagination {
    content: [
        File
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
