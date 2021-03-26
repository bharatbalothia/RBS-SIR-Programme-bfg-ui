import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AutorefreshDataComponent } from './autorefresh-data.component';

describe('AutorefreshDataComponent', () => {
  let component: AutorefreshDataComponent;
  let fixture: ComponentFixture<AutorefreshDataComponent>;

  beforeEach(async(() => {
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
