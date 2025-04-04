import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8443/api/v1';

  constructor(private http: HttpClient) {}

  // getCursos() {
  //   return this.http.get(`${this.apiUrl}/cursos`);
  // }
}
