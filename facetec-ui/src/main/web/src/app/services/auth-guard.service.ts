import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot } from '@angular/router';
import { FacetecService } from './facetec.service';

@Injectable()
export class AuthGuardService implements CanActivate {
  constructor(public auth: FacetecService, public router: Router) { }
  canActivate(route: ActivatedRouteSnapshot): boolean {
    if (!this.auth.isAuthenticated()) {
      this.router.navigate(['login'], { skipLocationChange: true });
      return false;
    }
    return true;
  }
}
