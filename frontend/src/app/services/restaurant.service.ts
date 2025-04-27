import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Restaurant } from '../dtos/restaurant.model'; // Asegúrate de definir el modelo en el archivo 'restaurant.model.ts'

@Injectable({
  providedIn: 'root'
})
export class RestaurantService {

  private apiUrl = 'api/v1/restaurants';

  constructor(private http: HttpClient) {}

  findAll(): Observable<Restaurant[]> {
    return this.http.get<Restaurant[]>(this.apiUrl);
  }

  findById(id: number): Observable<Restaurant> {
    return this.http.get<Restaurant>(`${this.apiUrl}/${id}`);
  }

  searchByLocation(query: string): Observable<Restaurant[]> {
    const params = new HttpParams().set('location', query);
    return this.http.get<Restaurant[]>(this.apiUrl, { params });
  }

  createRestaurant(restaurant: Restaurant): Observable<Restaurant> {
    return this.http.post<Restaurant>(this.apiUrl, restaurant);
  }

  updateRestaurant(id: number, restaurant: Restaurant): Observable<Restaurant> {
    return this.http.put<Restaurant>(`${this.apiUrl}/${id}`, restaurant);
  }

  deleteRestaurant(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  normalizeText(text: string): string {
    return text.normalize('NFD').replace(/[\u0300-\u036f]/g, '');
  }
}
