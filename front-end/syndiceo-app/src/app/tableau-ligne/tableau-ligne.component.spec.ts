/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { TableauLigneComponent } from './tableau-ligne.component';

describe('TableauLigneComponent', () => {
  let component: TableauLigneComponent;
  let fixture: ComponentFixture<TableauLigneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TableauLigneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableauLigneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
