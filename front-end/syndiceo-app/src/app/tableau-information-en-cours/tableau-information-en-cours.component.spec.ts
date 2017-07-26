/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { TableauInformationEnCoursComponent } from './tableau-information-en-cours.component';

describe('TableauInformationEnCoursComponent', () => {
  let component: TableauInformationEnCoursComponent;
  let fixture: ComponentFixture<TableauInformationEnCoursComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TableauInformationEnCoursComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableauInformationEnCoursComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
