import { Injectable } from '@angular/core';


const TOKEN_KEY = 'AuthToken';

@Injectable()
export class TokenStorage {

  constructor() { }

  signOut() {
    localStorage.removeItem('currentUser');
    localStorage.clear();
   // window.sessionStorage.removeItem(TOKEN_KEY);
   // window.sessionStorage.clear();
  }

  public saveToken(username: string, token: string) {
    localStorage.removeItem('currentUser');
    localStorage.setItem('currentUser', JSON.stringify({ username: username, token: token }));
    // window.sessionStorage.removeItem(TOKEN_KEY);
    // window.sessionStorage.setItem(TOKEN_KEY,  token);
  }

  public getToken(): string {
    var currentUser = JSON.parse(localStorage.getItem('currentUser'));
    var token = currentUser && currentUser.token;
    return token ? token : '';
   // return sessionStorage.getItem(TOKEN_KEY);
  }
}
