import { Injectable } from '@angular/core';

@Injectable()
export class DataService {

informations=[];
  constructor() { }

  getAllInformations(){
    this.informations = ["test naziff", "test naziff","test naziff","test naziff","test naziff"]

    return this.informations;
  }

}
