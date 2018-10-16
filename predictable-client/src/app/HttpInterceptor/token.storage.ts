import { Injectable } from '@angular/core';
import { User } from '../Models/user-model';


const TOKEN_KEY = 'AuthToken';

@Injectable()
export class TokenStorage {

  constructor() { }

  signOut() {
    localStorage.removeItem('currentUser');
    localStorage.clear();
  }

  public saveToken(username: string, token: string, user: User) {
    localStorage.removeItem('currentUser');
    localStorage.setItem('currentUser', JSON.stringify({ username: username, token: token, user: user }));
    // window.sessionStorage.removeItem(TOKEN_KEY);
    // window.sessionStorage.setItem(TOKEN_KEY,  token);
  }

  public getToken(): string {
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    const token = currentUser && currentUser.token;
    return token ? token : '';
   // return sessionStorage.getItem(TOKEN_KEY);
  }

  public getUser(): User {
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    const user = currentUser && currentUser.token && currentUser.user;
    return user ? user : '';
  }
}
