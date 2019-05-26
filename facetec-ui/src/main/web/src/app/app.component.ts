import { Component, ViewChild, ElementRef, AfterViewInit, Inject, AfterViewChecked, HostListener, OnInit } from '@angular/core';
import { MatBottomSheet, MatBottomSheetRef, MAT_BOTTOM_SHEET_DATA, MatIconRegistry } from '@angular/material';
import { Event, Router, NavigationStart, NavigationEnd, NavigationCancel, NavigationError, ActivatedRoute } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { FacetecService } from './services/facetec.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})

export class AppComponent implements OnInit {
  title = 'FACETEC - Interface FTCA-888';

  @ViewChild('appDrawer') appDrawer: ElementRef;

  constructor(
    private service: FacetecService, private activatedRoute: ActivatedRoute, private router: Router) {
  }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe(queryParam => {
      if (queryParam['token']) {
        this.service.afterAuthenticated(queryParam['token']);
      } else if (queryParam['errorType'] === "404" && queryParam['targetPath']) {
        let targetPath: string = queryParam['targetPath'];
        this.router.navigate([targetPath]);
      } else if (sessionStorage.getItem('logout')) {
        sessionStorage.removeItem('logout');
        this.router.navigate(['login'], { skipLocationChange: true });
      }
    })
  }

  isAuthenticated() {
    return this.service.isAuthenticated();
  }

  isAdmin() {
    return this.service.isAdmin();
  }
}
