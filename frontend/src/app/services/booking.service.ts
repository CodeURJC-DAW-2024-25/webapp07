import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Booking } from '../dtos/booking.model';
import { Observable } from 'rxjs';
import { UserDTO } from '../dtos/user.model';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  private apiUrl = '/api/v1/bookings';

  constructor(private http: HttpClient) {}

  getAllBookings(): Observable<Booking[]> {
    return this.http.get<Booking[]>(this.apiUrl);
  }

  getMyActiveBooking(): Observable<Booking> {
    return this.http.get<Booking>('/api/v1/bookings/me', { withCredentials: true });
  }

  createBooking(booking: Booking): Observable<Booking> {
    return this.http.post<Booking>(this.apiUrl, booking);
  }

  deleteBooking(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getAvailableSeats(restaurantId: number, date: string, shift: string): Observable<number> {
    return this.http.get<number>(`/api/v1/bookings/availability`, {
      params: { restaurantId, date, shift },
      withCredentials: true // <- Añade esto
    });
  }


  searchBookings(query: string): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.apiUrl}/search?query=${query}`);
  }
  getUserById(id: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`/api/v1/users/${id}`);
  }

  //getRestaurantById(id: number): Observable<RestaurantDTO> {
    //return this.http.get<RestaurantDTO>(`/api/v1/restaurants/${id}`);
  //}


}
