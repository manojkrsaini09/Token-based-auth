import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { AppService } from '../app.service';
import { MessageService } from 'primeng/api';


@Injectable()
export class ProductScheduleService {
    errorMessage: string;
    constructor(private route: Router, private http: HttpClient, private appService: AppService,
    private messageService: MessageService) {

    }

    getSchedules(): Observable<any> {
        return this.http.get(this.appService.apiUrl + '/schedule/all').pipe(
            tap( response => {
                console.log('schedule data:');
                console.log(response);
                if ( response['status'] === 'SUCCESS') {
                    this.errorMessage = response['data'].errorMessage;
                }
            }),
            catchError(this.handleError)
        );
    }
    private handleError(err: HttpErrorResponse) {
        console.log('error in user service');
        let errorMessage = '';
        if (err.error instanceof ErrorEvent) {
          errorMessage = `An error occurred: ${err.error.message}`;
        } else {
          errorMessage = `Server returned code: ${err.status}, error message is: ${err.message}`;
        }
        this.messageService.add({severity: 'error', summary: 'Error', detail: errorMessage});
        return throwError(errorMessage);
      }
}
