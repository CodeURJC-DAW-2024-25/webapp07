import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AppComponent} from "./components/app.component";
import { HomeComponent } from './components/viewsComponent/home/home.component';
import {LoginComponent} from "./components/viewsComponent/login/login.component";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: '**', redirectTo: '' },
  { path: 'login', component: LoginComponent }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
