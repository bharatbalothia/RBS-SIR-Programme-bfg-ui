import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { WorkflowStepWithPagination } from './workflow-step-with-pagination.model';

@Injectable({
    providedIn: 'root'
})
export class BusinessProcessService {

    private apiUrl: string = environment.apiUrl + 'workflow/';

    constructor(private http: HttpClient) {
    }

    getWorkflowSteps(id: number, params?: { page?: string, size?: string }) {
        return this.http.get<WorkflowStepWithPagination>(this.apiUrl + id + '/steps', { params });
    }

}
