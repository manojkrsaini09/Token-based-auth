import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import { Router } from '@angular/router';
@Component({
    templateUrl: 'dashboard.component.html'
})
export  class DashboardComponent implements OnInit {

    constructor(private app: AppService, private router: Router) {}

    ngOnInit() {
        if (!this.app.isLoggedIn()) {
            this.router.navigate(['/login']);
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
           }
        //    else {
        //     this.router.navigate(['/dashboard']);
        //    }
    }
}
