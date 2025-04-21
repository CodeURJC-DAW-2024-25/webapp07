import { Component, OnInit, OnDestroy } from '@angular/core';
import { TitleService } from '../../../services/title.service';
import { AuthService } from '../../../services/auth.service';
import { UserDTO } from '../../../dtos/user.model';
import { ScrollService } from '../../../services/scroll.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {
  private isInitialLoad = true;
  private scrollSubscription?: Subscription;

  constructor(
    private titleService: TitleService,
    private authService: AuthService,
    private scrollService: ScrollService
  ) {}

  ngOnInit() {
    this.setDynamicTitle();
  }

  ngAfterViewInit() {
    setTimeout(() => {
      this.setupScrollListener();
      this.isInitialLoad = false;
    }, 300);
  }

  private setDynamicTitle() {
    const hour = new Date().getHours();
    let greeting = 'Welcome to Voltereta Croqueta';

    this.authService.userData$.subscribe((user: UserDTO | null) => {
      if (user?.username) {
        if (hour < 12) greeting = `Good morning, ${user.username}`;
        else if (hour < 19) greeting = `Hello, ${user.username}`;
        else greeting = `Good evening, ${user.username}`;
      } else {
        if (hour < 12) greeting = 'Good morning at Voltereta Croqueta';
        else if (hour < 19) greeting = 'Welcome to Voltereta Croqueta';
        else greeting = 'Good evening at Voltereta Croqueta';
      }

      this.titleService.setTitle(greeting, true);
    });
  }

  private setupScrollListener() {
    this.scrollSubscription = this.scrollService.registerScrollCallback((direction, position) => {
      if (this.isInitialLoad) {
        this.titleService.setVisibility(true);
        return;
      }

      if (direction === 'down' && position > 100) {
        this.titleService.setVisibility(false);
      } else if (direction === 'up' || position <= 100) {
        this.titleService.setVisibility(true);
      }
    });
  }

  ngOnDestroy() {
    this.scrollSubscription?.unsubscribe();
  }
}
