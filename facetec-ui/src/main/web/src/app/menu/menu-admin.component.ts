import { Component } from '@angular/core';
import { FacetecService } from '../services/facetec.service';

@Component({
    selector: 'facetec-menu-admin',
    templateUrl: './menu-admin.component.html',
    styleUrls: ['./menu.component.scss']
})
export class MenuAdminComponent {

    user: string;

    constructor(private service: FacetecService) {
        this.user = service.getUser();
    }

    logout() {
        this.service.logout();
    }

}