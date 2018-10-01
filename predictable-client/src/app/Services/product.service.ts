import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { IProduct } from '../Models/productModel';
import 'rxjs/add/operator/map';
import { AppService } from '../app.service';


@Injectable()
export class ProductService {
    constructor(private http: HttpClient, private appService: AppService) {}

    getProducts(orgnizationId: number): Observable<IProduct[]> {
        return this.http.get<IProduct[]>(this.appService.apiUrl + 'product/all?companyId=' + orgnizationId);
    }

    saveProduct(product: IProduct): Observable<IProduct> {
        return this.http.post<IProduct>(this.appService.apiUrl + 'product/create', product);
    }

    deleteProduct(id: number): Observable<IProduct> {
        return this.http.get<IProduct>(this.appService.apiUrl + 'product/delete?id=' + id);
    }


    updateProduct(product: IProduct): Observable<IProduct> {
        return this.http.post<IProduct>(this.appService.apiUrl + 'product/update', product);
    }
}
