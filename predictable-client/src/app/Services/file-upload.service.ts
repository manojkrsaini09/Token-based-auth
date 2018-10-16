import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppService } from '../app.service';
import { MessageService } from 'primeng/api';

@Injectable()
export class FileUploadService {
   constructor(private http: HttpClient, private appService: AppService,  private messageService: MessageService) { }

   uploadFile(file: File, apiUrl: string): Observable<HttpEvent<string>> {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    const req = new HttpRequest('POST', this.appService.apiUrl + '' + apiUrl, formdata, {
      reportProgress: true,
      responseType: 'text'
    });
    return this.http.request(req);
  }
}
