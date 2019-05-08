import { Component, OnInit } from '@angular/core';
import { FacetecService } from '../services/facetec.service';
import { MatSnackBar } from '@angular/material';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  constructor(
    private service: FacetecService,
    private router: Router,
    private snackBar: MatSnackBar) { }

  jwtHelper = new JwtHelperService();
  username: string;
  password: string;
  ngOnInit() {
  }

  login(): void {
    this.service.login(this.username, this.password).subscribe(resp => {
      if (resp.headers.get('Failure')) {
        this.snackBar.open(resp.headers.get('Failure'), ' X ', { duration: 5000, panelClass: ['error-snackbar'] });
      } else {
        const token = resp.headers.get('Authorization');
        const tokenDecoded = this.jwtHelper.decodeToken(token);

        sessionStorage.setItem('token', token);
        sessionStorage.setItem('validUntil', this.jwtHelper.getTokenExpirationDate(token).getTime().toString());
        sessionStorage.setItem('user', tokenDecoded.sub);
        sessionStorage.setItem('image', tokenDecoded.GROUP);
        this.router.navigate(['cadastros']);
      }
    });
  }

  onKeydown(event) {
    if (event.key === 'Enter' && (this.username && this.password)) {
      this.login();
    }
  }
}

