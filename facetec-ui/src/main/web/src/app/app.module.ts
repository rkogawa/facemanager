import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule, ErrorHandler, LOCALE_ID } from '@angular/core';
import { AppComponent } from './app.component';
import { FacetecService } from './services/facetec.service';
import { AuthGuardService } from './services/auth-guard.service';
import { RoutingModule } from './routing.module';
import { LoginComponent } from './login/login.component';
import { TextMaskModule } from 'angular2-text-mask';
import { MaterialModule } from './material/material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CustomErrorHandler } from './error/custom-error-interceptor';
import { MenuComponent } from './menu/menu.component';
import { CadastrosComponent } from './cadastros/cadastros.component';
import { DatePickerComponent } from './shared/date-picker/date-picker.component';
import { DateFormatPipe } from './shared/date-picker/date-format.pipe';
import { FeedbackService } from './shared/feedback.service';
import { SetupComponent } from './setup/setup.component';
import { DeviceComponent } from './setup/device.component';
import { DeviceService } from './services/device.service';
import { AutocompleteComponent } from './shared/autocomplete/autocomplete.component';

@NgModule({
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    FormsModule,
    HttpClientModule,
    MaterialModule,
    ReactiveFormsModule,
    RoutingModule,
    TextMaskModule
  ],
  declarations: [
    AppComponent,
    AutocompleteComponent,
    CadastrosComponent,
    DatePickerComponent,
    DateFormatPipe,
    DeviceComponent,
    LoginComponent,
    MenuComponent,
    SetupComponent,
  ],
  bootstrap: [AppComponent],
  providers: [
    AuthGuardService,
    CustomErrorHandler,
    DateFormatPipe,
    DeviceService,
    FacetecService,
    FeedbackService
  ]
})
export class AppModule { }
