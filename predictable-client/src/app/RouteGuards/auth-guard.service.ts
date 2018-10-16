import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { AppService } from '../app.service';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private app: AppService, private router: Router) {}

  canActivate(): boolean {
    if (!this.app.isLoggedIn()) {
      // this.router.navigate(['login']);
      return false;
    }
    return true;
  }
}
