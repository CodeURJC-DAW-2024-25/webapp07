// src/app/app-routing.module.ts
import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { authGuard }            from './guards/auth.guard';
import { roleGuard }            from './guards/role.guard';

import { HomeComponent }        from './components/viewsComponent/home/home.component';
import { LoginComponent }       from './components/viewsComponent/login/login.component';
import { ProfileComponent }     from './components/viewsComponent/profile/profile.component';
import { MenuComponent }        from './components/viewsComponent/menu/menu.component';
import { CartComponent }        from './components/viewsComponent/order/order-cart/cart.component';
import { OrderHistoryComponent }from './components/viewsComponent/order/order-history/order-history.component';
import { OrderSummaryComponent} from './components/viewsComponent/order/order-summary/summary.component';
import { ConfirmPaymentComponent } from './components/viewsComponent/order/confirm-payment/confirm-payment.component';
import { DishDetailsComponent } from './components/viewsComponent/dish-details/dish-details.component';
import { DishFormComponent }    from './components/viewsComponent/dish-form/dish-form.component';
import { AboutComponent }       from './components/viewsComponent/about/about.component';
import { FaqsComponent }        from './components/viewsComponent/faqs/faqs.component';
import { UnauthorizedComponent} from './components/viewsComponent/unauthorized/unauthorized.component';

import { AdminManageUsersComponent } from './components/adminComponents/manage-users/manage-users.component';
import { AdminManageOrdersComponent} from './components/adminComponents/manage-orders/manage-orders.component';
import { AdminManageDishesComponent} from './components/adminComponents/manage-dishes/manage-dishes.component';
import { AdminManageBookingsComponent } from './components/adminComponents/manage-bookings/manage-bookings.component';

import { BookingFormComponent }      from './components/viewsComponent/booking/booking-form/booking-form.component';
import { BookingExistingComponent }  from './components/viewsComponent/booking/booking-messages/booking-existing.component';
import { BookingCancelledComponent } from './components/viewsComponent/booking/booking-messages/booking-cancelled.component';
import { BookingConfirmationComponent } from './components/viewsComponent/booking/booking-messages/booking-confirmation.component';
import {DishRatingComponent} from "./components/viewsComponent/dish-rating/dish-rating.component";

const routes: Routes = [
  // public
  {
    path: '',
    component: HomeComponent,
    data: { title: 'Voltereta Croqueta', showTitle: true }
  },
  {
    path: 'login',
    component: LoginComponent,
    data: { title: 'Login', showTitle: false }
  },
  { path: 'about-us', component: AboutComponent, data: { title: 'About us', showTitle: true } },
  { path: 'faqs',     component: FaqsComponent,  data: { title: 'FAQs',     showTitle: true } },
  {
    path: 'unauthorized',
    component: UnauthorizedComponent,
    data: { title: 'Unauthorized', showTitle: true }
  },
  { path: 'dishes',       component: MenuComponent, data: { title: 'Menu', showTitle: true } },
  { path: 'dishes/add',   component: DishFormComponent, data: { title: 'Dish Form', showTitle: false } },

  // login request
  {
    path: '',
    canActivate: [authGuard],
    children: [
      { path: 'profile',  component: ProfileComponent, data: { title: 'Profile', showTitle: true } },

      // menu

      { path: 'dishes/:id',   component: DishDetailsComponent, data: { title: 'Dish Info', showTitle: true } },
      { path: 'dishes/:id/edit', component: DishFormComponent, data: { title: 'Dish Form', showTitle: false } },
      { path: 'dishes/:id/rate', component: DishRatingComponent, data: { title: 'Dish Rate', showTitle: false } },

      // orders
      { path: 'orders/cart',          component: CartComponent },
      { path: 'orders/:id/summary',   component: OrderSummaryComponent },
      { path: 'orders/:id/confirm-payment', component: ConfirmPaymentComponent },
      { path: 'orders/history',       component: OrderHistoryComponent, data: { title: 'Orders History', showTitle: false } },

      // bookings
      { path: 'booking/form',          component: BookingFormComponent,      data: { title: 'Booking Form', showTitle: true } },
      { path: 'booking/existing',      component: BookingExistingComponent,  data: { title: 'Existing Reservation', showTitle: false } },
      { path: 'booking/cancelled',     component: BookingCancelledComponent, data: { title: 'Booking Cancelled',    showTitle: false } },
      { path: 'booking/confirmation',  component: BookingConfirmationComponent, data: { title: 'Reservation Confirmed', showTitle: false } },

      // **ADMIN**
      {
        path: 'admin/users',
        component: AdminManageUsersComponent,
        canActivate: [roleGuard],
        data: { title: 'Users', showTitle: true, roles: ['ADMIN'] }
      },
      {
        path: 'admin/orders',
        component: AdminManageOrdersComponent,
        canActivate: [roleGuard],
        data: { title: 'Orders', showTitle: true, roles: ['ADMIN'] }
      },
      {
        path: 'admin/dishes',
        component: AdminManageDishesComponent,
        canActivate: [roleGuard],
        data: { title: 'Dishes', showTitle: true, roles: ['ADMIN'] }
      },
      {
        path: 'admin/bookings',
        component: AdminManageBookingsComponent,
        canActivate: [roleGuard],
        data: { title: 'Booking Management', showTitle: true, roles: ['ADMIN'] }
      },
    ]
  },

  // wildcard
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
