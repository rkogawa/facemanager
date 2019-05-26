import { Component, OnInit, ViewChild } from '@angular/core';
import { FacetecService } from '../services/facetec.service';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { FeedbackService } from '../shared/feedback.service';
import { AsyncButtonDirective } from '../services/async-button.directive';
import { finalize } from 'rxjs/operators';


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

  username: string;
  password: string;

  @ViewChild('btnLogin') btnLogin: AsyncButtonDirective;

  login(): void {
    this.btnLogin.wait();
    this.service.login(this.username, this.password)
      .pipe(
        finalize(() => this.btnLogin.release())
      ).subscribe(resp => {
        if (resp.headers.get('Failure')) {
          this.feedbackService.showErrorMessage(resp.headers.get('Failure'));
        } else {
          const token = resp.headers.get('Authorization');
          this.service.afterAuthenticated(token);
        }
      });
  }

  disableButton() {
    return this.username === null || this.username.length === 0 || this.password === null || this.password.length === 0 || this.btnLogin.waiting;
  }

  onKeydown(event) {
    if (event.key === 'Enter' && (this.username && this.password)) {
      this.login();
    }
  }
}

