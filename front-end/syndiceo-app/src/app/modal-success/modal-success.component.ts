import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-modal-success',
  templateUrl: './modal-success.component.html',
  styleUrls: ['./modal-success.component.css']
})
export class ModalSuccessComponent implements OnInit {

  constructor(private router : Router) { }

  ngOnInit() {
  }

onClick(){
  this.router.navigate(['/']);
}
}
