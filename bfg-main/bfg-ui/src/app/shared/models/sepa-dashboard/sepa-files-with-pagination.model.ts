import { SEPAFile } from "./sepa-file.model";

export interface SEPAFilesWithPagination {
    content: [
        SEPAFile
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
