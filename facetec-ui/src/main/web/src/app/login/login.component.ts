import { Component, OnInit } from '@angular/core';
import { FacetecService } from '../services/facetec.service';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { FeedbackService } from '../shared/feedback.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  constructor(
    private service: FacetecService,
    private router: Router,
    private feedbackService: FeedbackService) { }

  jwtHelper = new JwtHelperService();
  username: string;
  password: string;

  login(): void {
    this.service.login(this.username, this.password).subscribe(resp => {
      if (resp.headers.get('Failure')) {
        this.feedbackService.showErrorMessage(resp.headers.get('Failure'));
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

