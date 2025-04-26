import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { OrderDTO } from '../dtos/order.model';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private baseUrl = '/api/v1/orders';

  constructor(private http: HttpClient) {}

  getAll(): Observable<OrderDTO[]> {
    return this.http.get<{ orders: OrderDTO[] }>(`${this.baseUrl}/`).pipe(
      map(response => response.orders)
    );
  }

  get(id: number): Observable<OrderDTO> {
    return this.http.get<OrderDTO>(`${this.baseUrl}/${id}`);
  }

  update(id: number, updates: Partial<OrderDTO>): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}`, updates);
  }

  updateStatus(id: number, newStatus: string): Observable<OrderDTO> {
    return this.http.patch<OrderDTO>(`${this.baseUrl}/${id}/status`, newStatus);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  updateOrderFields(orderId: number, fieldsToUpdate: { [key: string]: any }) {
    return this.http.patch(`/api/v1/orders/${orderId}`, fieldsToUpdate);
  }

  addToCart(dishId: number): Observable<OrderDTO> {
    return this.http.post<OrderDTO>(`${this.baseUrl}/cart`, { dishId });
  }

  viewCart(): Observable<OrderDTO> {
    return this.http.get<OrderDTO>(`${this.baseUrl}/cart`);
  }

  clearCart(): Observable<any> {
    // Se usa dishId=0 como valor simbólico porque el backend lo ignora
    return this.http.delete(`${this.baseUrl}/cart/dish`, {
      params: new HttpParams().set('dishId', '0')
    });
  }


  getHistory(): Observable<OrderDTO[]> {
    return this.http.get<any>(`${this.baseUrl}/history`).pipe(
      map(response => response.orders)
    );
  }

  getSummary(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}/summary`);
  }
}
