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
import {
  ConfirmationModalComponent
} from "./components/generalComponents/confirmation-modal/confirmation-modal.component";
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
    OrderHistoryComponent,
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
