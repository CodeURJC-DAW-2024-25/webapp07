//app-routing.module.ts

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AdminManageOrdersComponent} from "./components/adminComponents/manage-orders/manage-orders.component";
import {AppComponent} from "./components/app.component";
import { HomeComponent } from './components/viewsComponent/home/home.component';
import {LoginComponent} from "./components/viewsComponent/login/login.component";
import {ProfileComponent} from "./components/viewsComponent/profile/profile.component";
import {AdminManageUsersComponent} from "./components/adminComponents/manage-users/manage-users.component";
import { OrderHistoryComponent } from './components/viewsComponent/order/order-history/order-history.component';
import {MenuComponent} from "./components/viewsComponent/menu/menu.component";
import {CartComponent} from './components/viewsComponent/order/order-cart/cart.component';
import {OrderSummaryComponent} from './components/viewsComponent/order/order-summary/summary.component';


const routes: Routes = [
  { path: '', component: HomeComponent, data: {
      title: 'Voltereta Croqueta',
      showTitle: true
    }},
  { path: 'login', component: LoginComponent, data: {
      title: 'Login',
      showTitle: false
    } },
  {path:'profile', component: ProfileComponent, data: {
      title: 'Profile',
      showTitle: true
    }},
  { path:'admin/users', component: AdminManageUsersComponent, data: {
      title: 'Users',
      showTitle: true
    }},
  { path: 'admin/orders', component: AdminManageOrdersComponent, data: {
      title: 'Orders',
      showTitle: true
    } },
  { path: 'orders/history', component: OrderHistoryComponent, data: {
      title: 'Orders history',
      showTitle: false
    }},
  { path: 'dishes', component: MenuComponent, data: {
      title: 'Menu',
      showTitle: true
    }},
  { path: 'orders/cart', component: CartComponent },
  { path: 'orders/summary', component: OrderSummaryComponent },

  { path: '**', redirectTo: '' },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
