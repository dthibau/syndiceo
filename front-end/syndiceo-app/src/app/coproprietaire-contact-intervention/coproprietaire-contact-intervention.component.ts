import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-coproprietaire-contact-intervention',
  templateUrl: './coproprietaire-contact-intervention.component.html',
  styleUrls: ['./coproprietaire-contact-intervention.component.css']
})
export class CoproprietaireContactInterventionComponent implements OnInit {
  civilite="Mme";
  nom="nom";
  mail="contact@syndiceo.com";
  telephone="0674370647";
  mailCopy1="";
  mailCopy2;
  mailCopy3;

  constructor() { }

  ngOnInit() {
  }

}
