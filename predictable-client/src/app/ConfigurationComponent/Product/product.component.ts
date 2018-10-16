import {Component, OnInit} from '@angular/core';
import {IProduct} from '../../Models/productModel';
import {ProductService} from '../../Services/product.service';
import { AppService} from '../../app.service';
import {MessageService} from 'primeng/api';
import { OrganizationService } from '../../OrganizationComponent/organization.service';
import { Organization } from '../../Models/organization-model';
@Component({
    templateUrl : './product.component.html'
})
export class ProductsComponent implements OnInit {
    products: IProduct[];
    orgnizations: Organization[];
    errorMessage: string;
    editableMode = false;
    selectedProduct: IProduct;
    userOrgnizationId: number;
    isSuperAdmin = false;

    constructor(private productService: ProductService, private appService: AppService,
                private messageService: MessageService, private organizationService: OrganizationService) {}

    ngOnInit(): void {
        const loggedInUserInfo = this.appService.getLoggedInUser();
        this.isSuperAdmin = this.appService.isSuperAdmin();
        if (loggedInUserInfo.companyVO != null) {
                this.userOrgnizationId = loggedInUserInfo.companyVO.id;
        }
        this.getCompanies();
        this.getProducts();
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

    getProducts(): void {
        this.productService.getProducts(this.userOrgnizationId)
                .subscribe(products => {
                    this.products = products['data'];
                },
                err => {
                    this.messageService.add({severity: 'error', summary: 'Error', detail: 'Someting went wrong. Please try later'});
                });
    }

    addNewProduct(): void {
        this.selectedProduct = {} as IProduct;
        this.editableMode = true;
    }

    saveProduct(): void {
        if ( !this.isSuperAdmin ) {
            this.selectedProduct.companyId = this.userOrgnizationId;
        }
        if ( this.selectedProduct.id > 0) {
            this.productService.updateProduct(this.selectedProduct).subscribe(product => {
                this.selectedProduct = {} as IProduct;
                this.editableMode = false;

                this.getProducts();
                this.messageService.add({severity: 'info', summary: 'Success', detail: 'Updated Successfully'});
            },
            err => {
                this.messageService.add({severity: 'error', summary: 'Error', detail: 'Someting went wrong. Please try later'});
            });
        } else {
            this.productService.saveProduct(this.selectedProduct).subscribe(product => {
                this.selectedProduct = {} as IProduct;
                this.editableMode = false;

                this.getProducts();
                this.messageService.add({severity: 'info', summary: 'Success', detail: 'Saved Successfully'});
            },
            err => {
                this.messageService.add({severity: 'error', summary: 'Error', detail: 'Someting went wrong. Please try later'});
            });
        }
    }

    editProduct(product: IProduct): void {
        this.selectedProduct = product;
        this.editableMode = true;
    }

    cancel(): void {
        this.selectedProduct = {} as IProduct;
        this.editableMode = false;
    }

    deleteProduct(id: number): void {
        this.productService.deleteProduct(id).subscribe(product => {
            this.messageService.add({severity: 'info', summary: 'Success', detail: 'Deleted Successfully'});
            this.editableMode = false;
            this.getProducts();
        },
        err => {
            this.messageService.add({severity: 'error', summary: 'Error', detail: 'Someting went wrong. Please try later'});
        });
    }
}
