import { Component, OnInit } from '@angular/core';
import {DataService} from '../data.service';
import {Pipe, PipeTransform} from '@angular/core';


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
  filteredStatus="";
  datas=[];

  constructor(dataService:DataService) { 
   
    this.datas=dataService.getAllInformations();
  }

  ngOnInit() {
  }

}
