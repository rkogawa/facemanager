import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AuthGuardService } from './services/auth-guard.service';
import { CadastrosComponent } from './cadastros/cadastros.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    // data: { showNavbar: false, expectedRole: 'BÁSICO' },
  },
  {
    path: 'cadastros', component: CadastrosComponent,
    // data: { breadcrumb: 'Transação', expectedRole: 'BÁSICO' },
    canActivate: [AuthGuardService]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: []
})
export class RoutingModule { }
