import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { catchError, map } from 'rxjs/operators';
import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";
import { environment } from "../../environments/environment";
import { CustomErrorHandler } from "../error/custom-error-interceptor";
import { Router } from "@angular/router";

@Injectable()
export class FacetecService {

  private baseUrl = environment.baseUrl;

  constructor(private httpClient: HttpClient, private customErrorHandler: CustomErrorHandler, private router: Router) {
  }

  public login(user: string, password: string): Observable<any> {
    return this.httpClient.post<any>(`${this.baseUrl}/login`, { username: user, password: password },
      { ...this.getOptions(), observe: 'response' as 'response' }).pipe(
        catchError(this.handleError('login', null)));
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      this.customErrorHandler.handleError(error);
      return of(result as T);
    };
  }


  public isAuthenticated(): boolean {
    let ret = false;
    try {
      const until = sessionStorage.getItem('validUntil');
      ret = until === null ? false : new Date().getTime() <= Number.parseFloat(until);
      if (!ret && !this.router.isActive('login', true)) {
        this.logout();
      }
    } finally {
      return ret;
    }
  }

  public getUser() {
    return sessionStorage.getItem('user');
  }

  public logout() {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('user');
    sessionStorage.removeItem('validUntil');
    this.router.navigate(['login'], { skipLocationChange: true });
  }

  public create<I, O>(path: string, param: I): Observable<O> {
    return this.httpClient.post<O>(`${this.baseUrl}/${path}`, param, this.getOptions()).pipe(
      catchError(this.handleError('create', null))
    );
  }

  public update<I, O>(path: string, param: I): Observable<O> {
    return this.httpClient.put<O>(`${this.baseUrl}/${path}`, param, this.getOptions()).pipe(
      catchError(this.handleError('update', null))
    );
  }

  public get<T>(path: string, params?: any): Observable<T> {
    return this.httpClient.get<T>(`${this.baseUrl}/${path}`, this.getOptions(params)).pipe(
      catchError(this.handleError('get', null))
    );
  }

  public delete<O>(path: string, id: number): Observable<O> {
    return this.httpClient.delete<O>(`${this.baseUrl}/${path}/${id}`, this.getOptions()).pipe(
      catchError(this.handleError('delete', null))
    );
  }

  private getOptions(httpParams?: HttpParams) {
    return {
      headers: new HttpHeaders({
        'Authorization': sessionStorage.getItem('token') === null ? '' : sessionStorage.getItem('token')
      }),
      params: httpParams
    };
  }

}