//users.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UserDTO, LoginRequest } from '../dtos/user.model';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  private apiUrl = `${environment.apiBaseUrl}/api/v1/users`;

  constructor(private http: HttpClient) {}


  getAllUsers(query?: string): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(this.apiUrl, {
      params: query ? { query } : {}
    });
  }


  getUserById(id: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.apiUrl}/${id}`);
  }


  getCurrentUser(): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.apiUrl}/me`);
  }


  registerUser(userData: Omit<UserDTO, 'id'>): Observable<{id: number}> {
    return this.http.post<{id: number}>(`${this.apiUrl}/new`, userData);
  }


  updateUser(id: number, userData: Partial<UserDTO>): Observable<UserDTO> {
    return this.http.put<UserDTO>(`${this.apiUrl}/${id}`, userData);
  }


  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }


  banUser(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/banned`, {});
  }


  unbanUser(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/unbanned`, {});
  }
}
