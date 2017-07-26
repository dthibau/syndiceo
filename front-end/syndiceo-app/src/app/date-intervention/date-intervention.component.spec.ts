import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DateInterventionComponent } from './date-intervention.component';

describe('DateInterventionComponent', () => {
  let component: DateInterventionComponent;
  let fixture: ComponentFixture<DateInterventionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DateInterventionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DateInterventionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
