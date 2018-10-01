import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';


@Component({
  templateUrl: './login.component.html',
  styleUrls: [ './login.component.css']
})
 export class LoginComponent implements OnInit{
  pageTitle = 'Login Page';
  credentials = {username: '', password: ''};
  errorMessage = '';

  constructor(private app: AppService, private http: HttpClient, private router: Router) {
  }

  ngOnInit() {
      if (this.app.isLoggedIn()) {
        this.router.navigate(['/dashboard']);
      //   this.app.authenticate(undefined).subscribe(
      //     data => {
      //       console.log('data');
      //       if (this.app.authenticated) {
      //         console.log('navigate to dashboard');
      //           this.router.navigate(['/dashboard']);
      //       }
      //      },
      //     // error => this.errorMessage = <any> error
      //      error =>  this.errorMessage  = 'Invalid Credentials'
      //  );
       } else {
        this.router.navigate(['/login']);
       }
  }

  login() {
    this.app.authenticate(this.credentials).subscribe(
    data => {
      if (this.app.authenticated) {
           this.router.navigate(['/dashboard']);
          } else {
             this.errorMessage = 'Invalid Credentials';
          }
     },
     // error => this.errorMessage = <any> error
     error =>  this.errorMessage  = 'Invalid Credentials'
  );
  }
}
