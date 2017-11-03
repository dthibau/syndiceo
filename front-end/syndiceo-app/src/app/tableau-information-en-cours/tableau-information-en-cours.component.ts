import { Component, OnInit } from '@angular/core';
import {DataService} from '../data.service';

@Component({
  selector: 'app-tableau-information-en-cours',
  templateUrl: './tableau-information-en-cours.component.html',
  styleUrls: ['./tableau-information-en-cours.component.css'],
  providers: [DataService]
})
export class TableauInformationEnCoursComponent implements OnInit {
 
  numero;
  objet;
  dateCreation;
  domaine;
  demandeur;
  dateIntervention;
  immeuble;
  status;

  data=[];

  constructor(dataService:DataService) { 
    this.data=dataService.getAllInformations();
  }

  ngOnInit() {
  }

}
