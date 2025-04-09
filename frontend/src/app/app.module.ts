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

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    FooterComponent,
    HeaderComponent,
    LoginComponent,
    ProfileComponent,
    ConfirmationModalComponent,
    AdminManageUsersComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    HttpClientModule,
    CountUpModule,
    RouterModule,
    ReactiveFormsModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
