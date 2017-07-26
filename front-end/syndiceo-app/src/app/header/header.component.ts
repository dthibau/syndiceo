import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  private classeDemandeInformation = false;
  constructor() { }

  ngOnInit() {
  }

  setClasseDemandeInformation(info: boolean) {
    this.classeDemandeInformation = info;
  }

  isClasseDemandeInformation() {
    return this.classeDemandeInformation;
  }
}
