/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DetailInformationHistoriqueComponent } from './detail-information-historique.component';

describe('DetailInformationHistoriqueComponent', () => {
  let component: DetailInformationHistoriqueComponent;
  let fixture: ComponentFixture<DetailInformationHistoriqueComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetailInformationHistoriqueComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailInformationHistoriqueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
