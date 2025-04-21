import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TitleService {
  private titleSource = new BehaviorSubject<{title: string, visible?: boolean}>({title: '', visible: true});
  public title$ = this.titleSource.asObservable();

  /**
   * Actualiza el título y visibilidad
   * @param title Nuevo título
   * @param visible Si el título debe mostrarse (opcional)
   */
  setTitle(title: string, visible?: boolean) {
    this.titleSource.next({title, visible});
  }

  /**
   * Actualiza solo la visibilidad
   * @param visible Si el título debe mostrarse
   */
  setVisibility(visible: boolean) {
    const current = this.titleSource.value;
    this.titleSource.next({...current, visible});
  }
}
