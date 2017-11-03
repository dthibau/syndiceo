/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DetailInterventionInformationComponent } from './detail-intervention-information.component';

describe('DetailInterventionInformationComponent', () => {
  let component: DetailInterventionInformationComponent;
  let fixture: ComponentFixture<DetailInterventionInformationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetailInterventionInformationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailInterventionInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
