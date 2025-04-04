import { AppRoutingModule } from './app-routing.module';
import {NgModule} from "@angular/core";
import {AppComponent} from "./components/app.component";
import {HttpClientModule} from "@angular/common/http";
import {FooterComponent} from "./components/generalComponents/footer/footer.component";
import {BrowserModule} from "@angular/platform-browser";
import {HeaderComponent} from "./components/generalComponents/header/header.component";

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HeaderComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
