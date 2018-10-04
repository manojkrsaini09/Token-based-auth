import { Component, OnInit, Input } from '@angular/core';
import { HttpResponse, HttpEventType } from '@angular/common/http';
import { FileUploadService } from '../Services/file-upload.service';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
})
export class FileUploadComponent implements OnInit {
  selectedFiles: FileList;
  currentFileUpload: File;
  @Input() apiUrl: string;
  progress: { percentage: number } = { percentage: 0 };

  constructor(private uploadService: FileUploadService) { }
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
      }
    });
    this.selectedFiles = undefined;
  }
}
