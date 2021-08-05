import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FilesWithValueReportsDialogComponent } from './files-with-value-reports-dialog.component';

describe('FilesWithValueReportsDialogComponent', () => {
  let component: FilesWithValueReportsDialogComponent;
  let fixture: ComponentFixture<FilesWithValueReportsDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [FilesWithValueReportsDialogComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FilesWithValueReportsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
