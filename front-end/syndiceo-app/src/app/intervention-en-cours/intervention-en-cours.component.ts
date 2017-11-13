import { Component, OnInit } from '@angular/core';
import {DataService} from '../data.service';

@Component({
  selector: 'app-intervention-en-cours',
  templateUrl: './intervention-en-cours.component.html',
  styleUrls: ['./intervention-en-cours.component.css'],
  providers:[DataService]
})
export class InterventionEnCoursComponent implements OnInit {
 numero;
  objet;
  dateCreation;
  domaine;
  demandeur;
  dateIntervention;
  immeuble;
  status;
  filteredStatus="";
  datas=[];
  immeubles=[];
constructor(dataService:DataService) { 
   
    this.datas=dataService.getAllInteventionEnCours();
    this.immeubles=dataService.getAllImmeubles();
  }
  ngOnInit() {
  }

}
