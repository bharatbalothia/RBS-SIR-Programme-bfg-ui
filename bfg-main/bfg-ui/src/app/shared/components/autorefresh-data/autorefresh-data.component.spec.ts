import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { AutorefreshDataComponent } from './autorefresh-data.component';

describe('AutorefreshDataComponent', () => {
  let component: AutorefreshDataComponent;
  let fixture: ComponentFixture<AutorefreshDataComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ AutorefreshDataComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AutorefreshDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
