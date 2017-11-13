import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import {Routes,RouterModule} from "@angular/router";

import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { CorpsComponent } from './corps/corps.component';
import { FooterComponent } from './footer/footer.component';
import { DepotDemandeInterventionComponent } from './depot-demande-intervention/depot-demande-intervention.component';
import { DescriptionDemandeComponent } from './description-demande/description-demande.component';
import { DateInterventionComponent } from './date-intervention/date-intervention.component';
import { CoproprietaireContactInterventionComponent } from './coproprietaire-contact-intervention/coproprietaire-contact-intervention.component';
import { PieceJointeComponent } from './piece-jointe/piece-jointe.component';
import { DateComponent } from './date/date.component';
import { InterventionEnCoursComponent } from './intervention-en-cours/intervention-en-cours.component';
import { InterventionAGComponent } from './intervention-ag/intervention-ag.component';
import { InterventionArchiveComponent } from './intervention-archive/intervention-archive.component';
import { TableauComponent } from './tableau/tableau.component';
import { Menu2Component } from './menu2/menu2.component';
import { Menu1Component } from './menu1/menu1.component';
import { DepotDemandeInformationComponent } from './depot-demande-information/depot-demande-information.component';
import { InformationEnCoursComponent } from './information-en-cours/information-en-cours.component';

import { TableauInformationEnCoursComponent } from './tableau-information-en-cours/tableau-information-en-cours.component';
import { TableauInformationArchiveComponent } from './tableau-information-archive/tableau-information-archive.component';
import { TableauLigneComponent } from './tableau-ligne/tableau-ligne.component';
import { ModalAccessDeniedComponent } from './modal-access-denied/modal-access-denied.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { ModalSuccessComponent } from './modal-success/modal-success.component';
import { InformationArchiveComponent } from './information-archive/information-archive.component';
import { DetailInterventionComponent } from './detail-intervention/detail-intervention.component';
import { DetailInformationComponent } from './detail-information/detail-information.component';
import { FenetreActionComponent } from './fenetre-action/fenetre-action.component';
import { DetailInformationHistoriqueComponent } from './detail-information-historique/detail-information-historique.component';
import { MenuDetailInformationComponent } from './menu-detail-information/menu-detail-information.component';
import { DetailInterventionInformationComponent } from './detail-intervention-information/detail-intervention-information.component';
import { DetailInterventionHistoriqueComponent } from './detail-intervention-historique/detail-intervention-historique.component';
import { MenuDetailInterventionComponent } from './menu-detail-intervention/menu-detail-intervention.component';
import { FiltreTabPipe } from './filtre-tab.pipe';


const appRoutes: Routes =[
  { path: 'depot-demande-intervention', component: DepotDemandeInterventionComponent },
  { path: 'intervention-en-cours', component: InterventionEnCoursComponent },
  { path: 'intervention-ag', component: InterventionAGComponent },
  { path: 'intervention-archive', component: InterventionArchiveComponent },
  { path: 'depot-demander-information', component: DepotDemandeInformationComponent },
  { path: 'information-en-cours', component: InformationEnCoursComponent },
  { path: 'information-archive', component: InformationArchiveComponent },
  { path: 'detail-information', component: DetailInformationComponent},
  { path: 'detail-information-historique', component: DetailInformationHistoriqueComponent},
  { path: 'detail-intervention', component: DetailInterventionComponent},
  { path: 'detail-intervention-information', component: DetailInterventionInformationComponent},
  { path: 'detail-intervention-historique', component: DetailInterventionHistoriqueComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    CorpsComponent,
    FooterComponent,
    DateInterventionComponent,
    CoproprietaireContactInterventionComponent,
    PieceJointeComponent,
    DateComponent,
    InterventionEnCoursComponent,
    InterventionAGComponent,
    InterventionArchiveComponent,
    TableauComponent,
    Menu2Component,
    Menu1Component,
    DepotDemandeInterventionComponent,
    DepotDemandeInformationComponent,
    DescriptionDemandeComponent,
    InformationEnCoursComponent,
    TableauInformationEnCoursComponent,
    TableauInformationArchiveComponent,
    TableauLigneComponent,
    ModalAccessDeniedComponent,
    NotFoundComponent,
    ModalSuccessComponent,
    InformationArchiveComponent,
    DetailInterventionComponent,
    DetailInformationComponent,
    FenetreActionComponent,
    DetailInformationHistoriqueComponent,
    MenuDetailInformationComponent,
    DetailInterventionInformationComponent,
    DetailInterventionHistoriqueComponent,
    MenuDetailInterventionComponent,
    FiltreTabPipe
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
