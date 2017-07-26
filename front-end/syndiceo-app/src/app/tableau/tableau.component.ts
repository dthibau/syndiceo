import { Component, OnInit } from '@angular/core';
import { TableauLigneComponent } from '../tableau-ligne/tableau-ligne.component';


@Component({
  selector: 'app-tableau',
  templateUrl: './tableau.component.html',
  styleUrls: ['./tableau.component.css']
})
export class TableauComponent implements OnInit {

  ligne = new TableauLigneComponent;

  lignes = [] ;
  
  constructor() { 

    this.ligne.numero = 1;
    this.ligne.demandeur = "test naziff";
    this.ligne.dateCreation="01/04/2012";
    this.ligne.domaine = "Ascenseur";
    this.ligne.objet = "M.Local";
    this.ligne.dateIntervention="27/04/2012";
    this.ligne.immeuble="9991, 41 BD DE CRETEIL";
    this.ligne.status="Default";

    this.lignes.push(this.ligne);
    this.lignes.push(this.ligne);
    this.lignes.push(this.ligne);
    this.lignes.push(this.ligne);


  }

  ngOnInit() {
  }

}
