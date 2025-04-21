import { Injectable } from '@angular/core';
import { fromEvent, Subscription } from 'rxjs';
import { debounceTime, throttleTime } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ScrollService {
  private lastScrollPosition = 0;
  private scrollCallbacks: ((direction: 'up'|'down', position: number) => void)[] = [];
  private scrollSubscription: Subscription;

  constructor() {
    this.scrollSubscription = fromEvent(window, 'scroll').pipe(
      throttleTime(16, undefined, { leading: true, trailing: true }),
      debounceTime(50)
    ).subscribe(() => {
      requestAnimationFrame(() => {
        const scrollPosition = window.scrollY || window.pageYOffset;
        const scrollDirection = scrollPosition > this.lastScrollPosition ? 'down' : 'up';

        this.lastScrollPosition = scrollPosition;

        this.scrollCallbacks.forEach(callback => {
          callback(scrollDirection, scrollPosition);
        });
      });
    });
  }

  registerScrollCallback(callback: (direction: 'up'|'down', position: number) => void): Subscription {
    this.scrollCallbacks.push(callback);


    return new Subscription(() => {
      this.scrollCallbacks = this.scrollCallbacks.filter(cb => cb !== callback);
    });
  }

  getScrollDirection(scrollPosition: number): 'up'|'down' {
    return scrollPosition > this.lastScrollPosition ? 'down' : 'up';
  }

  ngOnDestroy() {
    this.scrollSubscription.unsubscribe();
  }
}
