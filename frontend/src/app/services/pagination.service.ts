import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class PaginationService {
  private pageSource = new BehaviorSubject<number>(0);
  currentPage = this.pageSource.asObservable();

  constructor() { }

  incrementPage() {
    this.pageSource.next(this.pageSource.value + 1);
  }
  resetPage() {
    this.pageSource.next(0);
  }
}
