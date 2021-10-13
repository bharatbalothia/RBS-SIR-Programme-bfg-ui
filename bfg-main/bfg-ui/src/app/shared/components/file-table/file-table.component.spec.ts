import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { FileTableComponent } from './file-table.component';

describe('FileTableComponent', () => {
  let component: FileTableComponent;
  let fixture: ComponentFixture<FileTableComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ FileTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FileTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
