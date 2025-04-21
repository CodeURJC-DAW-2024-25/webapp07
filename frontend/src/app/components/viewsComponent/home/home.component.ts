import { Component, OnInit, HostListener } from '@angular/core';
import { TitleService } from '../../../services/title.service';
import { AuthService } from '../../../services/auth.service';
import { UserDTO } from '../../../dtos/user.model';
import {debounceTime, throttleTime} from 'rxjs/operators';
import {fromEvent, map} from "rxjs";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  private lastScrollPosition = 0;
  private isInitialLoad = true;

  constructor(
    private titleService: TitleService,
    private authService: AuthService
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
        // if user
        if (hour < 12) greeting = `Good morning, ${user.username}`;
        else if (hour < 19) greeting = `Hello, ${user.username}`;
        else greeting = `Good evening, ${user.username}`;
      } else {
        // for guest
        if (hour < 12) greeting = 'Good morning at Voltereta Croqueta';
        else if (hour < 19) greeting = 'Welcome to Voltereta Croqueta';
        else greeting = 'Good evening at Voltereta Croqueta';
      }

      this.titleService.setTitle(greeting, true);
    });
  }

  private setupScrollListener() {
    fromEvent(window, 'scroll').pipe(
      throttleTime(16, undefined, { leading: true, trailing: true }), // ~60fps
      debounceTime(50)
    ).subscribe(() => {
      requestAnimationFrame(() => {
        const scrollPosition = window.scrollY || window.pageYOffset;
        const scrollDirection = scrollPosition > this.lastScrollPosition ? 'down' : 'up';


        if (this.isInitialLoad) {
          this.titleService.setVisibility(true);
          return;
        }


        if (scrollDirection === 'down' && scrollPosition > 100) {
          this.titleService.setVisibility(false);
        } else if (scrollDirection === 'up' || scrollPosition <= 100) {
          this.titleService.setVisibility(true);
        }

        this.lastScrollPosition = scrollPosition;
      });
    });
  }


  @HostListener('window:scroll', ['$event'])
  onWindowScroll() {
    if (this.isInitialLoad) return;
    requestAnimationFrame(() => {

    });
  }
}
