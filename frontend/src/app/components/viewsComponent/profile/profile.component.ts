import { Component, OnInit } from '@angular/core';
import { UsersService } from '../../../services/users.service';
import { UserDTO } from '../../../dtos/user.model';
import {AuthService} from "../../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user!: UserDTO;
  editMode = false;
  isLoading = true;

  constructor(
    private usersService: UsersService,
    private authService: AuthService,
    private router: Router) {}

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    this.usersService.getCurrentUser().subscribe({
      next: (user) => {
        this.user = user;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading profile:', err);
        this.isLoading = false;
      }
    });
  }

  toggleEditMode(): void {
    this.editMode = !this.editMode;
  }

  saveProfile(): void {
    this.isLoading = true;
    this.usersService.updateUser(this.user).subscribe({
      next: () => {
        this.editMode = false;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error updating profile:', err);
        this.isLoading = false;
      }
    });
  }
  logout(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Logout failed:', err);
      }
    });
  }
  get today(): string {
    return new Date().toISOString().split('T')[0];
  }
}
