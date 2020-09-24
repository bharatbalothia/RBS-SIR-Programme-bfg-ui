import { WorkflowStep } from './workflow-step.model';

export interface WorkflowStepWithPagination {
    content: [
        WorkflowStep
    ];
    status: string;
    state: string;
    fullTracking: string;
    pageable: {
        pageSize: number;
        pageNumber: number;
        offset: number;
    };
    size: number;
    totalElements: number;
    totalPages: number;
}