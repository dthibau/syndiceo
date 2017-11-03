import { Component, OnInit } from '@angular/core';
import {DataService} from '../data.service';

@Component({
  selector: 'app-tableau-ligne',
  templateUrl: './tableau-ligne.component.html',
  styleUrls: ['./tableau-ligne.component.css'],
  providers: [DataService]
})
export class TableauLigneComponent implements OnInit {
  
  numero;
  objet;
  dateCreation;
  domaine;
  demandeur;
  dateIntervention;
  immeuble;
  status;

  constructor() { 
   
  }

  ngOnInit() {
  }

}
