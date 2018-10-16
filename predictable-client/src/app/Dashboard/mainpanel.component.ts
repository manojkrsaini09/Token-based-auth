import { Component, OnInit } from '@angular/core';
import { ProductScheduleService } from '../Services/product-schedule.service';
import { MessageService } from 'primeng/api';

@Component({
    templateUrl: 'mainpanel.component.html'
})
export class MainPanelComponent implements OnInit {
    uploadUrl = '/schedule/upload';
    schedules = [];
    ngOnInit(): void {
        this.getSchedule();
    }
    constructor(private productScheduleService: ProductScheduleService, private messageService: MessageService) {
    }

    getSchedule(): void {
        this.productScheduleService.getSchedules().subscribe(
            response => {
               console.log(response);
               if (response['status'] === 'SUCCESS') {
                this.schedules = response['data'];
               } else {
                   this.messageService.add({severity: 'error', summary: 'Error', detail: response.exception.message}) ;
               }
            }
        );
    }
    handleResponse(res): void {
        console.log('getting response of upload>>>');
        console.log(res);
        if ( res['status'] === 'SUCCESS' ) {
            this.schedules.push(res.data);
            this.messageService.add({severity: 'success', summary: 'Successfully Processed',
             detail:  'Schedule Upload Task has been initiated.'} );
        } else {
            this.messageService.add({severity: 'error', summary: 'Error', detail: res.exception.message}) ;
        }
    }
}
