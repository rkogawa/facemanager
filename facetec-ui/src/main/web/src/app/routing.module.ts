import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AuthGuardService } from './services/auth-guard.service';
import { CadastrosComponent } from './cadastros/cadastros.component';
import { SetupComponent } from './setup/setup.component';
import { AuthGuardAdminService } from './services/auth-guard-admin.service';
import { UsuariosTabsComponent } from './usuarios/usuarios-tabs.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'cadastros', component: CadastrosComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'setup', component: SetupComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'usuarios', component: UsuariosTabsComponent,
    canActivate: [AuthGuardAdminService]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: []
})
export class RoutingModule { }
