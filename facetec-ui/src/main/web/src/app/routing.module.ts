import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    // data: { showNavbar: false, expectedRole: 'BÁSICO' },
  },
  // {
  //   path: 'transacao', component: TransacaoComponent,
  //   data: { breadcrumb: 'Transação', expectedRole: 'BÁSICO' },
  //   canActivate: [AuthGuardService]
  // },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: []
})
export class RoutingModule { }
