//title.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TitleService {
  private titleSource = new BehaviorSubject<{title: string, visible?: boolean}>({title: '', visible: true});
  public title$ = this.titleSource.asObservable();

  setTitle(title: string, visible?: boolean) {
    this.titleSource.next({title, visible});
  }

  setVisibility(visible: boolean) {
    const current = this.titleSource.value;
    this.titleSource.next({...current, visible});
  }
}
