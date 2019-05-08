import { Component, ViewChild, ElementRef, AfterViewInit, Inject, AfterViewChecked, HostListener, OnInit } from '@angular/core';
import { MatBottomSheet, MatBottomSheetRef, MAT_BOTTOM_SHEET_DATA, MatIconRegistry } from '@angular/material';
import { Event, Router, NavigationStart, NavigationEnd, NavigationCancel, NavigationError } from '@angular/router';
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
    private service: FacetecService, private router: Router) {
  }

  ngOnInit() {
    if (!this.isAuthenticated()) {
      this.router.navigate(['login'], { skipLocationChange: true });
    }
  }

  isAuthenticated() {
    console.log('check is authenticated');
    return this.service.isAuthenticated();
  }
}
