import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-detail-intervention-historique',
  templateUrl: './detail-intervention-historique.component.html',
  styleUrls: ['./detail-intervention-historique.component.css']
})
export class DetailInterventionHistoriqueComponent implements OnInit {

  date="13/03/2014";
  objet="intervention conforme";
  emetteur= "Mme Peroux";
  recepteur= "Mme TROIS";
  statut='votre demande "9992-10" "pannee ascenseur" du "13/03/2014" est cloturée' ;

  constructor() { }

  ngOnInit() {
  }

}
