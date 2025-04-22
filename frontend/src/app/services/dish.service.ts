import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UserDTO, LoginRequest } from '../dtos/user.model';
import { DishDTO } from '../dtos/dish.model';


@Injectable({
  providedIn: 'root'
})
export class DishService {
  private readonly apiUrl = `/api/v1/dishes`;

  constructor(private http: HttpClient) {}

  getDishes(): Observable<DishDTO[]> {
    return this.http.get<DishDTO[]>(`${this.apiUrl}/`);
  }

  getDishById(id: number): Observable<DishDTO> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }
  addDish(dishData: Omit<DishDTO, 'id'>): Observable<{id: number}> {
    return this.http.post<{id: number}>(`${this.apiUrl}/`, dishData);
  }

  updateDish(id: number, dishData: Omit<DishDTO, 'id'>): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, dishData);
  }

  disableDish(id: number) {
    return this.http.patch(`/api/v1/dishes/${id}/disabled`, { available: false });
  }

  enableDish(id: number) {
    return this.http.patch(`/api/v1/dishes/${id}/enabled`, { available: true });
  }


}
