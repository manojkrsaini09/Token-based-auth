import {Component, OnInit} from '@angular/core';
import {ILine} from '../../Models/lineModel';
import { IOrgnization } from '../../Models/companyModel';
import {LineService} from '../../Services/line.service';
import { AppService} from '../../app.service';
import {MessageService} from 'primeng/api';
import { OrganizationService } from '../../OrganizationComponent/organization.service';
import { Organization } from '../../Models/organization-model';
@Component({
    templateUrl : './line.component.html'
})
export class LinesComponent implements OnInit {
    lines: ILine[];
    orgnizations: Organization[];
    errorMessage: string;
    editableMode = false;
    selectedLine: ILine;
    isSuperAdmin = false;
    userOrgnizationId = 0;

    constructor(private lineService: LineService, private organizationService: OrganizationService,
        private appService: AppService, private messageService: MessageService) {}

    ngOnInit(): void {
        const loggedInUserInfo = this.appService.getLoggedInUser();
        this.isSuperAdmin = this.appService.isSuperAdmin();
        if (loggedInUserInfo.companyVO != null) {
                this.userOrgnizationId = loggedInUserInfo.companyVO.id;
        }
        this.getCompanies();
        this.getLines();
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

    getLines(): void {
        this.lines = this.lineService.getLines();
    }

    addNewLine(): void {
        this.selectedLine = {} as ILine;
        this.editableMode = true;
    }

    saveLine(): void {
        if ( !this.isSuperAdmin) {
            this.selectedLine.companyId = this.userOrgnizationId;
        }
        if ( this.selectedLine.id > 0 ) {
            this.lineService.updateLine(this.selectedLine);
            this.messageService.add({severity: 'info', summary: 'Success', detail: 'Updated Successfully'});
        } else {
            this.lineService.saveLine(this.selectedLine);
            this.messageService.add({severity: 'info', summary: 'Success', detail: 'Saved Successfully'});
        }
        this.selectedLine = {} as ILine;
        this.editableMode = false;

        this.getLines();
    }

    editLine(line: ILine): void {
        this.selectedLine = line;
        this.editableMode = true;
    }

    cancel(): void {
        this.selectedLine = {} as ILine;
        this.editableMode = false;
    }

    deleteLine(id: number): void {
        this.lineService.deleteLine(id);
        this.messageService.add({severity: 'info', summary: 'Success', detail: 'Deleted Successfully'});
        this.editableMode = false;
        this.getLines();
    }
}
