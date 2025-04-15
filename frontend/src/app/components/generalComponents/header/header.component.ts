//header.component.ts

import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  isLoggedIn$ = this.authService.isAuthenticated$;
  isAdmin$ = this.authService.isAdmin$;


  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {

  }

  handleAuthAction() {
    if (this.authService.currentAuthStatus) {
      this.authService.logout().subscribe(() => {
        this.router.navigate(['/']);
      });
    } else {
      this.router.navigate(['/login']);
    }
  }
}
