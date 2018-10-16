import {Component, OnInit} from '@angular/core';
import {IEventType} from '../../Models/eventTypeModel';
import { IOrgnization } from '../../Models/companyModel';
import {EventTypeService} from '../../Services/eventType.service';
import { AppService} from '../../app.service';
import {MessageService} from 'primeng/api';
import { Organization } from '../../Models/organization-model';
import { OrganizationService } from '../../OrganizationComponent/organization.service';
@Component({
    templateUrl : './eventType.component.html'
})
export class EventTypeComponent implements OnInit {
    eventTypes: IEventType[];
    orgnizations: Organization[];
    errorMessage: string;
    editableMode = false;
    selectedEventType: IEventType;
    isSuperAdmin = false;
    userOrgnizationId = 0;

    constructor(private eventTypeService: EventTypeService, private appService: AppService,
                private messageService: MessageService, private organizationService: OrganizationService) {}

    ngOnInit(): void {
        const loggedInUserInfo = this.appService.getLoggedInUser();
        this.isSuperAdmin = this.appService.isSuperAdmin();
        if (loggedInUserInfo.companyVO != null) {
                this.userOrgnizationId = loggedInUserInfo.companyVO.id;
        }
        this.getCompanies();
        this.getEventTypes();
    }

    getCompanies(): void {
        this.organizationService.getOrganizations().subscribe(
            response => {
               console.log(response);
               if (response['status'] === 'SUCCESS') {
                this.orgnizations = response['data'];
               } else {
                // this.errorMessage = "";
               }
            },
            error => this.errorMessage = 'Error in call'
        );
    }
    getEventTypes(): void {
        this.eventTypes = this.eventTypeService.getEventTypes();
    }

    addNewEventType(): void {
        this.selectedEventType = {} as IEventType;
        this.editableMode = true;
    }

    saveEventType(): void {
        if ( !this.isSuperAdmin ) {
            this.selectedEventType.companyId = this.userOrgnizationId;
        }
        if ( this.selectedEventType.id > 0 ) {
            this.eventTypeService.updateEventType(this.selectedEventType);
            this.messageService.add({severity: 'info', summary: 'Success', detail: 'Updated Successfully'});
        } else {
            this.eventTypeService.saveEventType(this.selectedEventType);
            this.messageService.add({severity: 'info', summary: 'Success', detail: 'Saved Successfully'});
        }
        this.selectedEventType = {} as IEventType;
        this.editableMode = false;
        this.getEventTypes();
    }

    editEventType(event: IEventType): void {
        this.selectedEventType = event;
        this.editableMode = true;
    }

    cancel(): void {
        this.selectedEventType = {} as IEventType;
        this.editableMode = false;
    }

    deleteEventType(id: number): void {
        this.eventTypeService.deleteEventType(id);
        this.messageService.add({severity: 'info', summary: 'Success', detail: 'Deleted Successfully'});
        this.editableMode = false;
        this.getEventTypes();
    }
}
