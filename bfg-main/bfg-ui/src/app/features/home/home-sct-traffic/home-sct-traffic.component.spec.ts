import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeSCTTrafficComponent } from './home-sct-traffic.component';

describe('HomeSCTTrafficComponent', () => {
  let component: HomeSCTTrafficComponent;
  let fixture: ComponentFixture<HomeSCTTrafficComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HomeSCTTrafficComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeSCTTrafficComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
