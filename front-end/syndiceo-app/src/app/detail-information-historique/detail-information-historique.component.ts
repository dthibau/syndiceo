import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-detail-information-historique',
  templateUrl: './detail-information-historique.component.html',
  styleUrls: ['./detail-information-historique.component.css']
})
export class DetailInformationHistoriqueComponent implements OnInit {

  date = "28/11/2013 15:57";
  objet = "Déposer une demande d'information";
  emetteur = "Mme TOPIN";
  recepteur = "Mme FRAISSIGNES";
  message = "Une demande d'information vous concernant a été déposé";
  signature="Cordialement";

  constructor(private router: Router) { }

  ngOnInit() {
  }

  onLoadPage(){
    this.router.navigate(['/']);

  }

}
