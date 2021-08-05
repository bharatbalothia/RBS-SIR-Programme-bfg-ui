import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FilesWithValueComponent } from './files-with-value.component';

describe('FilesWithValueComponent', () => {
  let component: FilesWithValueComponent;
  let fixture: ComponentFixture<FilesWithValueComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FilesWithValueComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FilesWithValueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
