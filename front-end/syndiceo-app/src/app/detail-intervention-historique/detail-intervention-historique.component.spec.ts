/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DetailInterventionHistoriqueComponent } from './detail-intervention-historique.component';

describe('DetailInterventionHistoriqueComponent', () => {
  let component: DetailInterventionHistoriqueComponent;
  let fixture: ComponentFixture<DetailInterventionHistoriqueComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetailInterventionHistoriqueComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailInterventionHistoriqueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
