import { WorkflowStep } from './workflow-step.model';

export interface WorkflowStepWithPagination {
    content: [
        WorkflowStep
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