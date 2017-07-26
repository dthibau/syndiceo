import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CoproprietaireContactInterventionComponent } from './coproprietaire-contact-intervention.component';

describe('CoproprietaireContactInterventionComponent', () => {
  let component: CoproprietaireContactInterventionComponent;
  let fixture: ComponentFixture<CoproprietaireContactInterventionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CoproprietaireContactInterventionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CoproprietaireContactInterventionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
