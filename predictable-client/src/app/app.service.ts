import { Injectable, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { throwError, Observable} from 'rxjs';
import { tap , catchError , finalize } from 'rxjs/operators';
import { User } from './Models/user-model';
import { TokenStorage } from './HttpInterceptor/token.storage';

@Injectable()
export class AppService {
  // loggedInUserInfo: User;
  apiUrl = '/api/';
  errorMessage = '';

  constructor(private http: HttpClient, private router: Router,
    private token: TokenStorage) {}

  // authenticate (credentials): Observable<any> {
  //       const headers = new HttpHeaders(credentials ? {
  //           authorization : 'Basic ' + btoa(credentials.username + ':' + credentials.password)
  //       } : {});


  //       return  this.http.get('userInfo', {headers: headers}).pipe(
  //           tap(response => {
  //               if ( response['status'] === 'SUCCESS') {
  //                   this.authenticated = response['data'].authenticated;
  //                   this.loggedInUserInfo = response['data'].userVO;
  //                   this.errorMessage = response['data'].errorMessage;
  //                   console.log('this.loggedInUserInfo');
  //                   console.log( this.loggedInUserInfo);
  //               } else {
  //                   this.authenticated = false;
  //                   this.errorMessage = response['data'].errorMessage;
  //               }
  //            }
  //           ),
  //           catchError(this.handleError)
  //         );
  //   }

  isLoggedIn(): boolean {
    const token: String = this.token.getToken();
    return token && token.length > 0;
  }

  getLoggedInUser(): User {
    const user: User = this.token.getUser();
    return user;
  }

  isSuperAdmin(): boolean {
    const user = this.getLoggedInUser();
    if ( user.roles != null &&
      user.roles.length > 0 &&
      user.roles[0].role === 'superadmin') {
      return true;
    } else {
      return false;
    }
  }
  authenticate (credentials): Observable<any> {
    console.log('this.apiUrl');
    console.log(this.apiUrl);
    return  this.http.post<any>(this.apiUrl + 'token/generate-token', credentials).pipe(
        tap(response => {
            if ( response['status'] === 'SUCCESS') {
             // this.loggedInUserInfo = response['data'].userVO;
              this.token.saveToken(credentials.username, response['data'].token, response['data'].userVO);
              this.errorMessage = response['data'].errorMessage;
            } else {
                this.errorMessage = response['data'].errorMessage;
            }
         }
        ),
        catchError(this.handleError)
      );
}

    logout() {
        // this.http.post('/logout', {}).pipe(
        //   tap(
        //     data => {
        //       console.log('All: ' + JSON.stringify(data));
        //       this.token.signOut();
        //       this.authenticated = false;
        //     }
        // ),
        //   catchError(this.handleError)
        // ).subscribe();
        this.token.signOut();
        this.router.navigateByUrl('/login');
      }

      private handleError(err: HttpErrorResponse) {
        console.log('error in login');
        this.token.signOut();
        this.router.navigate(['/login']);
        // in a real world app, we may send the server to some remote logging infrastructure
        // instead of just logging it to the console
        let errorMessage = '';
        if (err.error instanceof ErrorEvent) {
          errorMessage = `An error occurred: ${err.error.message}`;
        } else {
          errorMessage = `Server returned code: ${err.status}, error message is: ${err.message}`;
        }
        console.error(errorMessage);
        return throwError(errorMessage);
      }
}
