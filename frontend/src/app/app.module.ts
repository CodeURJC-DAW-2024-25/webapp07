//app.module.ts

import { AppRoutingModule } from './app-routing.module';
import {NgModule} from "@angular/core";
import {AppComponent} from "./components/app.component";
import {HttpClientModule} from "@angular/common/http";
import {FooterComponent} from "./components/generalComponents/footer/footer.component";
import {BrowserModule} from "@angular/platform-browser";
import {HeaderComponent} from "./components/generalComponents/header/header.component";
import {HomeComponent} from "./components/viewsComponent/home/home.component";
import { CountUpModule } from 'ngx-countup';
import {LoginComponent} from "./components/viewsComponent/login/login.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";
import {ProfileComponent} from "./components/viewsComponent/profile/profile.component";
import {ConfirmationModalComponent} from "./components/generalComponents/confirmation-modal/confirmation-modal.component";
import {AdminManageUsersComponent} from "./components/adminComponents/manage-users/manage-users.component";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ToastrModule} from "ngx-toastr";
import {
  AdminManageOrdersComponent
} from "./components/adminComponents/manage-orders/manage-orders.component";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from "@angular/material/table";
import {MatButton} from "@angular/material/button";
import {OrderHistoryComponent} from "./components/viewsComponent/order/order-history/order-history.component";
import {TitleComponent} from "./components/generalComponents/title/title.component";
import {MenuComponent} from "./components/viewsComponent/menu/menu.component";
import {DishDetailsComponent} from "./components/viewsComponent/dish-details/dish-details.component";
import {DishFormComponent} from "./components/viewsComponent/dish-form/dish-form.component";
import {CartComponent} from "./components/viewsComponent/order/order-cart/cart.component";
import {OrderSummaryComponent} from "./components/viewsComponent/order/order-summary/summary.component";
import { ConfirmPaymentComponent } from './components/viewsComponent/order/confirm-payment/confirm-payment.component';
import {DishFilterComponent} from "./components/viewsComponent/dish-filter/dish-filter.component";
import {AdminManageDishesComponent} from "./components/adminComponents/manage-dishes/manage-dishes.component";

import { BookingFormComponent } from './components/viewsComponent/booking/booking-form/booking-form.component';
import { BookingExistingComponent } from './components/viewsComponent/booking/booking-messages/booking-existing.component';
import { BookingCancelledComponent } from './components/viewsComponent/booking/booking-messages/booking-cancelled.component';
import { BookingConfirmationComponent } from './components/viewsComponent/booking/booking-messages/booking-confirmation.component';
import {AdminManageBookingsComponent} from "./components/adminComponents/manage-bookings/manage-bookings.component";
import {AboutComponent} from "./components/viewsComponent/about/about.component";
import {TeamComponent} from "./components/viewsComponent/team/team.component";
import {ChefCardComponent} from "./components/viewsComponent/chef-card/chef-card.component";
import {CounterComponent} from "./components/generalComponents/counter/counter.component";
import {FaqsComponent} from "./components/viewsComponent/faqs/faqs.component";
import {AccordionItemComponent} from "./components/generalComponents/accordion-item/accordion-item.component";
import {TestimonialCardComponent} from "./components/generalComponents/testimonial-card/testimonial-card.component";
import {UnauthorizedComponent} from "./components/viewsComponent/unauthorized/unauthorized.component";
import {DishRatingComponent} from "./components/viewsComponent/dish-rating/dish-rating.component";
import {OrderInfoComponent} from "./components/viewsComponent/order/order-info/order-info.component";

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    FooterComponent,
    HeaderComponent,
    LoginComponent,
    ProfileComponent,
    ConfirmationModalComponent,
    AdminManageUsersComponent,
    AdminManageOrdersComponent,
    AdminManageDishesComponent,
    OrderHistoryComponent,
    TitleComponent,
    MenuComponent,
    CartComponent,
    OrderSummaryComponent,
    ConfirmPaymentComponent,
    MenuComponent,
    DishDetailsComponent,
    DishFormComponent,
    DishFilterComponent,
    DishRatingComponent,
    BookingFormComponent,
    BookingExistingComponent,
    BookingCancelledComponent,
    BookingConfirmationComponent,
    AdminManageBookingsComponent,
    AboutComponent,
    TeamComponent,
    ChefCardComponent,
    CounterComponent,
    FaqsComponent,
    AccordionItemComponent,
    TestimonialCardComponent,
    UnauthorizedComponent,
    OrderInfoComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    HttpClientModule,
    CountUpModule,
    RouterModule,
    ReactiveFormsModule,
    FormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      positionClass: 'toast-top-right',
      timeOut: 3000
    }),
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatHeaderCellDef,
    MatCellDef,
    MatButton,
    MatHeaderRow,
    MatRow,
    MatHeaderRowDef,
    MatRowDef,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
