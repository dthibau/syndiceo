import { Component, OnInit } from '@angular/core';
import {DataService} from '../data.service';

@Component({
  selector: 'app-information-archive',
  templateUrl: './information-archive.component.html',
  styleUrls: ['./information-archive.component.css'],
  providers: [DataService]
})
export class InformationArchiveComponent implements OnInit {
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
