import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { HttpResponse, HttpEventType, HttpErrorResponse } from '@angular/common/http';
import { FileUploadService } from '../Services/file-upload.service';
import { AppService } from '../app.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
})
export class FileUploadComponent implements OnInit {
  selectedFiles: FileList;
  currentFileUpload: File;
  @Input() apiUrl: string;
  @Output() uploadResponse = new EventEmitter();
  progress: { percentage: number } = { percentage: 0 };

  constructor(private uploadService: FileUploadService, private messageService: MessageService) { }
  ngOnInit() {
  }
  selectFile(event) {
    this.selectedFiles = event.target.files;
  }
  upload() {
    this.progress.percentage = 0;
    this.currentFileUpload = this.selectedFiles.item(0);
    this.uploadService.uploadFile(this.currentFileUpload, this.apiUrl).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * event.loaded / event.total);
      } else if (event instanceof HttpResponse) {
        console.log('File is completely uploaded!');
        console.log(event.body);
        const resJson = JSON.parse(event.body);
        this.uploadResponse.emit(resJson);
        // this.messageService.add({severity: 'success', summary: 'Successfully Processed',
        //  detail:  resJson.data});
       // var resJson = JSON.parse(event.body);
      }
    },
    error => {
      this.messageService.add({severity: 'error', summary: 'Error', detail: 'Error In File Upload'});
      console.log('in error response');
          console.log('error status>>>');
          console.log(error.status);
          console.log(error.message);
  });
    this.selectedFiles = undefined;
  }
}
