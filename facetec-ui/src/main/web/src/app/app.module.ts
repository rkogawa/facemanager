import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule, ErrorHandler, LOCALE_ID } from '@angular/core';
import { AppComponent } from './app.component';
import { FacetecService } from './services/facetec.service';
import { AuthGuardService } from './services/auth-guard.service';
import { RoutingModule } from './routing.module';
import { LoginComponent } from './login/login.component';
import { MaterialModule } from './material/material.module';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CustomErrorHandler } from './error/custom-error-interceptor';

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    MaterialModule,
    RoutingModule
  ],
  declarations: [
    AppComponent,
    LoginComponent
  ],
  bootstrap: [AppComponent],
  providers: [
    FacetecService,
    AuthGuardService,
    CustomErrorHandler
  ]
})
export class AppModule { }
