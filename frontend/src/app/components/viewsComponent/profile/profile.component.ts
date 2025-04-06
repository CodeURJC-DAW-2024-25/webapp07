import { Component, OnInit } from '@angular/core';
import { UsersService } from '../../../services/users.service';
import { UserDTO } from '../../../dtos/user.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user!: UserDTO;
  editMode = false;
  isLoading = true;

  constructor(private usersService: UsersService) {}

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
    const { id, ...userData } = this.user;
    this.usersService.updateUser(id, userData).subscribe({
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
}
