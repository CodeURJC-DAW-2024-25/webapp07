import { Component, OnInit } from '@angular/core';
import { UsersService } from '../../../services/users.service';
import { UserDTO } from '../../../dtos/user.model';

@Component({
  selector: 'app-admin-manage-users',
  templateUrl: './manage-users.component.html',
  styleUrls: ['./manage-users.component.css']
})
export class AdminManageUsersComponent implements OnInit {
  public users: UserDTO[] = [];
  public isLoading = true;
  public searchQuery: string = '';

  public showConfirmationModal = false;
  public modalTitle = '';
  public modalMessage = '';
  public selectedUser: UserDTO | null = null;
  public actionType: 'BAN' | 'UNBAN' | null = null;

  constructor(private usersService: UsersService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.isLoading = true;
    this.usersService.getAllUsers(this.searchQuery).subscribe({
      next: (data) => {
        this.users = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading users:', err);
        this.isLoading = false;
      }
    });
  }

  confirmBan(user: UserDTO): void {
    this.selectedUser = user;
    this.actionType = 'BAN';
    this.modalTitle = 'Confirm Ban';
    this.modalMessage = `Are you sure you want to ban user "${user.username}"?`;
    this.showConfirmationModal = true;
  }

  confirmUnban(user: UserDTO): void {
    this.selectedUser = user;
    this.actionType = 'UNBAN';
    this.modalTitle = 'Confirm Unban';
    this.modalMessage = `Are you sure you want to unban user "${user.username}"?`;
    this.showConfirmationModal = true;
  }

  onActionConfirmed(): void {
    if (!this.selectedUser || !this.actionType) return;

    const userId = this.selectedUser.id;
    const action$ = this.actionType === 'BAN'
      ? this.usersService.banUser(userId)
      : this.usersService.unbanUser(userId);

    action$.subscribe({
      next: () => {
        this.loadUsers();
        this.resetModalState();
      },
      error: (err) => {
        console.error(`Error performing ${this.actionType} action:`, err);
        this.resetModalState();
      }
    });
  }

  resetModalState(): void {
    this.showConfirmationModal = false;
    this.modalTitle = '';
    this.modalMessage = '';
    this.selectedUser = null;
    this.actionType = null;
  }
}
