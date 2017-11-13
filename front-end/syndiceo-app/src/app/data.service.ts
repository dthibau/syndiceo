import { Injectable } from '@angular/core';

@Injectable()
export class DataService {

  informations = [];
  immeubles = [];
  constructor() { }

  getAllInformations() {
    this.informations = [
      { numero: '1', objet: 'test naziff', dateCreation: ' 01/04/2012', domaine: 'Ascenseur', demandeur: 'M.Local', dateIntervention: '27/04/2012', immeuble: '9991, 41 BD DE CRETEIL', status: 'test naziff' },
      { numero: '1', objet: 'test david', dateCreation: ' 01/04/2012', domaine: 'Ascenseur', demandeur: 'M.Local', dateIntervention: '27/04/2012', immeuble: '9991, 41 BD DE CRETEIL', status: 'test naziff' },
      { numero: '1', objet: 'test carole', dateCreation: ' 01/04/2012', domaine: 'Ascenseur', demandeur: 'M.Local', dateIntervention: '27/04/2012', immeuble: '9991, 41 BD DE CRETEIL', status: 'test naziff' },
      { numero: '1', objet: 'test patrick', dateCreation: ' 01/04/2012', domaine: 'Ascenseur', demandeur: 'M.Local', dateIntervention: '27/04/2012', immeuble: '9991, 41 BD DE CRETEIL', status: 'test naziff' }
    ]

    return this.informations;
  }


  getAllInteventionEnCours() {
    this.informations = [
      { numero: '1', objet: 'test naziff', dateCreation: ' 01/04/2012', domaine: 'Ascenseur', demandeur: 'M.Local', dateIntervention: '27/04/2012', immeuble: '9991, 41 BD DE CRETEIL', status: 'Default' },
      { numero: '1', objet: 'test david', dateCreation: ' 01/04/2012', domaine: 'Ascenseur', demandeur: 'M.Local', dateIntervention: '27/04/2012', immeuble: '9991, 41 BD DE CRETEIL', status: 'Default' },
      { numero: '1', objet: 'test carole', dateCreation: ' 01/04/2012', domaine: 'Ascenseur', demandeur: 'M.Local', dateIntervention: '27/04/2012', immeuble: '9991, 41 BD DE CRETEIL', status: 'Default' },
      { numero: '1', objet: 'test patrick', dateCreation: ' 01/04/2012', domaine: 'Ascenseur', demandeur: 'M.Local', dateIntervention: '27/04/2012', immeuble: '9991, 41 BD DE CRETEIL', status: 'Default' }
    ]

    return this.informations;
  }

  getAllImmeubles() {
    this.immeubles = [
      {
        nom: 'immeuble 1',
        escalier: 'grand'
      },
      {
        nom: 'immeuble 2',
        escalier: 'grand'
      },
      {
        nom: 'immeuble 3',
        escalier: 'grand'
      },
      {
        nom: 'immeuble 4',
        escalier: 'grand'
      },
      {
        nom: 'immeuble 1',
        escalier: 'grand'
      },
      {
        nom: 'immeuble 2',
        escalier: 'grand'
      },
      {
        nom: 'immeuble 3',
        escalier: 'grand'
      },
      {
        nom: 'immeuble 4',
        escalier: 'grand'
      }
    ]
    return this.immeubles;
  }

}
