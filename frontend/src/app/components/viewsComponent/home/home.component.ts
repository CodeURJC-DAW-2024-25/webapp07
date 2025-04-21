import { Component, OnInit, HostListener } from '@angular/core';
import { TitleService } from '../../../services/title.service';
import { AuthService } from '../../../services/auth.service';
import { UserDTO } from '../../../dtos/user.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  private lastScrollPosition = 0;

  constructor(
    private titleService: TitleService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.setDynamicTitle();
    this.setupScrollListener();
  }

  private setDynamicTitle() {
    const hour = new Date().getHours();
    let greeting = 'Welcome';
    let username = '';

    // Get current user if authenticated
    this.authService.userData$.subscribe((user: UserDTO | null) => {
      if (user?.username) {
        username = `, ${user.username}`;
      }

      if (hour < 12) greeting = 'Good morning';
      else if (hour < 19) greeting = 'Good evening';
      else greeting = 'Good night';

      this.titleService.setTitle(`${greeting}${username} to Voltereta Croqueta`, true);
    });
  }

  @HostListener('window:scroll')
  onWindowScroll() {
    const currentScrollPosition = window.scrollY;

    // Only update if scroll position changes significantly
    if (Math.abs(currentScrollPosition - this.lastScrollPosition) > 50) {
      this.titleService.setVisibility(currentScrollPosition < 100);
      this.lastScrollPosition = currentScrollPosition;
    }
  }

  private setupScrollListener() {
    // Initial check
    this.onWindowScroll();
  }
}
