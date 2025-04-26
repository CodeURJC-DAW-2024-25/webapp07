//dish.service.ts

import {HttpClient, HttpParams} from '@angular/common/http';
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

  getAllDishes(query?: string): Observable<DishDTO[]> {
    return this.http.get<DishDTO[]>(this.apiUrl, {
      params: query ? { query } : {}
    });
  }



  getDishes(page: number, size: number, filters?: any): Observable<DishDTO[]> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if (filters) {
      if (filters.name) params = params.set('name', filters.name);
      if (filters.maxPrice) params = params.set('maxPrice', filters.maxPrice);
      if (filters.ingredient) params = params.set('ingredient', filters.ingredient);
    }

    return this.http.get<DishDTO[]>('/api/v1/dishes', { params });
  }



  getDishById(id: number): Observable<DishDTO> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }
  addDish(dishData: Omit<DishDTO, 'id'>): Observable<DishDTO> {
    return this.http.post<DishDTO>(`${this.apiUrl}/`, dishData);
  }

  updateDish(id: number, dishData: Partial<DishDTO>): Observable<DishDTO> {
    // this.http.put<DishDTO>(`${this.apiUrl}/${id}/image`, dishData.im);
    return this.http.put<DishDTO>(`${this.apiUrl}/${id}`, dishData);
  }

  uploadDishImage(id: number, file: File): Observable<void> {
    const form = new FormData();
    form.append('imageFile', file);
    return this.http.post<void>(`${this.apiUrl}/${id}/image`, form);
  }

  replaceDishImage(id: number, file: File): Observable<void> {
    const form = new FormData();
    form.append('imageFile', file);
    return this.http.put<void>(`${this.apiUrl}/${id}/image`, form);
  }

  disableDish(id: number) {
    return this.http.patch(`/api/v1/dishes/${id}/disabled`, { available: false });
  }

  enableDish(id: number) {
    return this.http.patch(`/api/v1/dishes/${id}/enabled`, { available: true });
  }


}
