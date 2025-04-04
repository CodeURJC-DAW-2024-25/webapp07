import { AppRoutingModule } from './app-routing.module';
import {NgModule} from "@angular/core";
import {AppComponent} from "./components/app.component";
import {HttpClientModule} from "@angular/common/http";
import {FooterComponent} from "./components/generalComponents/footer/footer.component";
import {BrowserModule} from "@angular/platform-browser";
import {HeaderComponent} from "./components/generalComponents/header/header.component";
import {HomeComponent} from "./components/viewsComponent/home/home.component";
import { CountUpModule } from 'ngx-countup';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    FooterComponent,
    HeaderComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    HttpClientModule,
    CountUpModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
