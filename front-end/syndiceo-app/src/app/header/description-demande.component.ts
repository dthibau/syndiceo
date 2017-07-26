import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-description-demande',
  templateUrl: './description-demande.component.html',
  styleUrls: ['./description-demande.component.css']
})
export class DescriptionDemandeComponent implements OnInit {

  objet;
  immeuble;
  domaine;
  description;
  batiment;
  localisation;

  constructor() { }

  ngOnInit() {
  }

}
