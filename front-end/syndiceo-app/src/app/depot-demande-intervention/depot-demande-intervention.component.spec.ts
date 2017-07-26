import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DepotDemandeInterventionComponent } from './depot-demande-intervention.component';

describe('DepotDemandeInterventionComponent', () => {
  let component: DepotDemandeInterventionComponent;
  let fixture: ComponentFixture<DepotDemandeInterventionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DepotDemandeInterventionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DepotDemandeInterventionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
