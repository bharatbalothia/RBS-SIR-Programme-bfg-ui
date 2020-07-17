import { TrustedCertificate } from './trusted-certificate.model';

export interface TrustedCertificatesWithPagination {
    content: [
        TrustedCertificate
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
