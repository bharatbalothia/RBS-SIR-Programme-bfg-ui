import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { take } from 'rxjs/operators';
import { ErrorMessage, getApiErrorMessage } from 'src/app/core/utils/error-template';
import { File } from 'src/app/shared/models/file/file.model';
import { FileService } from 'src/app/shared/models/file/file.service';

@Component({
  selector: 'app-file-monitor',
  templateUrl: './file-monitor.component.html',
  styleUrls: ['./file-monitor.component.scss']
})
export class FileMonitorComponent implements OnInit {

  errorMessage: ErrorMessage;
  isLoading = false;

  files: File[];
  dataSource: MatTableDataSource<File>;

  constructor(
    private fileService: FileService
  ) { }

  ngOnInit(): void {
    this.getFileList();
  }

  setLoading(data) {
    this.errorMessage = null;
    this.isLoading = true;
    return data;
  }


  getFileList() {
    this.fileService.getFileMonitorList().pipe(data => this.setLoading(data)).pipe(take(1)).subscribe((data: File[]) => {
      this.isLoading = false;
      this.files = data;
      this.updateTable();
    },
      (error) => {
        this.isLoading = false;
        this.errorMessage = getApiErrorMessage(error);
      });
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.files);
  }

}
