import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { take } from 'rxjs/operators';
import { FILE_BP_STATE } from 'src/app/shared/models/file/file-constants';
import { File } from 'src/app/shared/models/file/file.model';
import { FileService } from 'src/app/shared/models/file/file.service';
import { FilesWithPagination } from 'src/app/shared/models/file/files-with-pagination.model';

@Component({
  selector: 'app-error-monitor',
  templateUrl: './error-monitor.component.html',
  styleUrls: ['./error-monitor.component.scss']
})
export class ErrorMonitorComponent implements OnInit {

  isLoading = false;

  files: FilesWithPagination;
  dataSource: MatTableDataSource<File>;
  pageIndex = 0;
  pageSize = 100;

  constructor(
    private fileService: FileService,
  ) { }

  ngOnInit(): void {
    this.getFileList(this.pageIndex, this.pageSize);
  }

  setLoading(data) {
    this.isLoading = true;
    return data;
  }

  getFileList(pageIndex: number, pageSize: number) {
    const formData = {
      page: pageIndex.toString(),
      size: pageSize.toString(),
      bpstate: FILE_BP_STATE.RED
    };

    this.fileService.getFileList(formData)
      .pipe(data => this.setLoading(data))
      .pipe(take(1)).subscribe((data: FilesWithPagination) => {
        this.isLoading = false;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.files = data;
        this.updateTable();
      },
        error => this.isLoading = false
      );
  }

  updateTable() {
    this.dataSource = new MatTableDataSource(this.files.content);
  }

}
