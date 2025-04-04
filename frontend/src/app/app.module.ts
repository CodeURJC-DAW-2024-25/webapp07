import { AppRoutingModule } from './app-routing.module';
import {NgModule} from "@angular/core";
import {AppComponent} from "./components/app.component";
import {HttpClientModule} from "@angular/common/http";
import {FooterComponent} from "./components/generalComponents/footer/footer.component";
import {BrowserModule} from "@angular/platform-browser";

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    HttpClientModule
  ],
})
export class AppModule {}
