import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { BusinessProcessDocumentContent } from './business-process-document-content.model';
import { BusinessProcessHeader } from './business-process-header.model';
import { BusinessProcess } from './business-process.model';
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

    getBPHeader(params: { wfdID, wfdVersion }) {
        return this.http.get<BusinessProcessHeader>(this.apiUrl + 'bp-header', { params });
    }

    getBPDetails(identifier: string) {
        return this.http.get<BusinessProcess>(this.apiUrl + 'bp-details', { params: { identifier } });
    }

    getDocumentContent(id: string) {
        return this.http.get<BusinessProcessDocumentContent>(this.apiUrl + 'document-content', { params: { id } });
    }
}
