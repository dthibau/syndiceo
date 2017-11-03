/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { FenetreActionComponent } from './fenetre-action.component';

describe('FenetreActionComponent', () => {
  let component: FenetreActionComponent;
  let fixture: ComponentFixture<FenetreActionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FenetreActionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FenetreActionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
