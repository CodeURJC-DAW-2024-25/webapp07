//login.component.ts

import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { UsersService } from '../../../services/users.service';
import {RegisterRequest, LoginRequest, UserDTO} from '../../../dtos/user.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  registerForm: FormGroup;
  isLoading = false;
  errorMessage: string | null = null;
  isRegistering = false;
  registerSuccess = false;
  registerErrorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private usersService: UsersService,
    public router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });

    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3)]],
      passwordConfirm: ['', [Validators.required]],
      dateOfBirth: ['', [Validators.required]]
    }, { validator: this.passwordMatchValidator });
  }

  passwordMatchValidator(form: FormGroup) {
    return form.get('password')?.value === form.get('passwordConfirm')?.value
      ? null : { mismatch: true };
  }

  onLogin() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = null;

    const credentials: LoginRequest = {
      username: this.loginForm.value.username,
      password: this.loginForm.value.password
    };

    this.authService.login(credentials).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.message || 'Login failed. Please try again.';
        this.loginForm.get('password')?.reset();
      }
    });
  }

  onRegister() {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.isRegistering = true;
    this.registerErrorMessage = null;

    const registerData: RegisterRequest = {
      username: this.registerForm.value.username,
      email: this.registerForm.value.email,
      password: this.registerForm.value.password,
      passwordConfirm: this.registerForm.value.passwordConfirm,
      dateOfBirth: this.registerForm.value.dateOfBirth
    };

    const userRegistrationData: Omit<UserDTO, 'id'> = {
      username: registerData.username,
      email: registerData.email,
      password: registerData.password,
      dateOfBirth: new Date(registerData.dateOfBirth)
    };

    this.usersService.registerUser(userRegistrationData).subscribe({
      next: () => {
        this.isRegistering = false;
        this.registerSuccess = true;
        this.registerForm.reset();
        setTimeout(() => {
          this.registerSuccess = false;
        }, 3000);
      },
      error: (err) => {
        this.isRegistering = false;
        this.registerErrorMessage = err.error?.message || 'Registration failed. Please try again.';
      }
    });
  }

  get username() { return this.loginForm.get('username'); }
  get password() { return this.loginForm.get('password'); }
  get regUsername() { return this.registerForm.get('username'); }
  get regEmail() { return this.registerForm.get('email'); }
  get regPassword() { return this.registerForm.get('password'); }
  get regPasswordConfirm() { return this.registerForm.get('passwordConfirm'); }
  get regDateOfBirth() { return this.registerForm.get('dateOfBirth'); }
}
