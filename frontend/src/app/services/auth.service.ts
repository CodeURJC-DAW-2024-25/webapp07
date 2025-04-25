//auth.service.ts

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, catchError, tap, throwError } from 'rxjs';
import { UserDTO, LoginRequest } from '../dtos/user.model';
import jwt_decode from 'jwt-decode';

export interface AuthResponse {
  status: 'SUCCESS' | 'ERROR';
  message?: string;
  token?: string;
}
export interface DecodedToken {
  sub: string;
  roles: string[];
  exp: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = `/api/v1/auth`;

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  private userDataSubject = new BehaviorSubject<UserDTO | null>(null);
  public userData$ = this.userDataSubject.asObservable();

  private isAdminSubject = new BehaviorSubject<boolean>(false);
  public isAdmin$ = this.isAdminSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    this.checkAuthStatus();
  }


  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials, {
      withCredentials: true
    }).pipe(
      tap(response => this.handleLoginSuccess(response)),
      catchError(error => this.handleAuthError(error))
    );
  }

  refreshToken(): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/refresh`, {}, {
      withCredentials: true
    }).pipe(
      tap(response => this.handleRefreshSuccess(response)),
      catchError(error => this.handleAuthError(error))
    );
  }

  logout(): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/logout`, {}, {
      withCredentials: true
    }).pipe(
      tap(() => this.handleLogoutSuccess()),
      catchError(error => this.handleAuthError(error))
    );
  }

  private handleLoginSuccess(response: AuthResponse): void {
    if (response.status === 'SUCCESS') {
      this.isAuthenticatedSubject.next(true);
      this.fetchUserInfo();
      this.router.navigate(['/']);
    }
  }

  private fetchUserInfo(): void {
    this.http.get<UserDTO>(`/api/v1/users/me`, { withCredentials: true })
      .subscribe({
        next: (user: UserDTO) => {
          this.userDataSubject.next(user);
          const isAdmin = (user.roles ?? []).includes('ADMIN');
          this.isAdminSubject.next(isAdmin);        },
        error: () => {
          this.userDataSubject.next(null);
          this.isAdminSubject.next(false);
        }
      });
  }

  private handleRefreshSuccess(response: AuthResponse): void {
    if (response.status === 'SUCCESS') {
      this.isAuthenticatedSubject.next(true);
    }
  }

  private handleLogoutSuccess(): void {
    this.isAuthenticatedSubject.next(false);
    this.router.navigate(['/login']);
  }

  private handleAuthError(error: any): Observable<never> {
    this.isAuthenticatedSubject.next(false);
    const errorMessage = error.error?.message || 'Authentication failed';
    return throwError(() => new Error(errorMessage));
  }

  private checkAuthStatus(): void {
    this.refreshToken().subscribe({
      next: () => this.fetchUserInfo(),
      error: () => {  }
    });
  }

  get roles(): string[] {
    return this.userDataSubject.value?.roles ?? [];
  }

  hasAnyRole(allowed: string[]): boolean {
    return allowed.some(r => this.roles.includes(r));
  }

  get currentAuthStatus(): boolean {
    return this.isAuthenticatedSubject.value;
  }
}
