import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-date',
  templateUrl: './date.component.html',
  styleUrls: ['./date.component.css']
})
export class DateComponent implements OnInit {
   dateInfo: any;
   test;

  constructor() {
console.log(this.dateInfo);

   }

  ngOnInit() {
  }

  onUpdateDate(event: Event){
    this.dateInfo=(<HTMLInputElement>event.target).value;
    console.log(event);
  }
  getDate(){
    this.dateInfo;
  }

}
