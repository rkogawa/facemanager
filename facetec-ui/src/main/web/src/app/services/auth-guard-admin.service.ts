import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot } from '@angular/router';
import { FacetecService } from './facetec.service';
import { FeedbackService } from '../shared/feedback.service';

@Injectable()
export class AuthGuardAdminService implements CanActivate {
  constructor(public auth: FacetecService, public router: Router, private feedbackService: FeedbackService) { }
  canActivate(route: ActivatedRouteSnapshot): boolean {
    if (!this.auth.isAuthenticated()) {
      this.router.navigate(['login'], { skipLocationChange: true });
      return false;
    } else if (!this.auth.isAdmin()) {
      this.feedbackService.showErrorMessage('Usuário não tem permissão para acessar esta página.');
      return false;
    }
    return true;
  }
}
