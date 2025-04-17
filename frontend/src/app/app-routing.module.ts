//app-routing.module.ts

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AdminManageOrdersComponent} from "./components/adminComponents/manage-orders/manage-orders.component";
import {AppComponent} from "./components/app.component";
import { HomeComponent } from './components/viewsComponent/home/home.component';
import {LoginComponent} from "./components/viewsComponent/login/login.component";
import {ProfileComponent} from "./components/viewsComponent/profile/profile.component";
import {AdminManageUsersComponent} from "./components/adminComponents/manage-users/manage-users.component";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  {path:'profile', component: ProfileComponent},
  { path:'admin/users', component: AdminManageUsersComponent},
  { path: 'admin/orders', component: AdminManageOrdersComponent },
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
