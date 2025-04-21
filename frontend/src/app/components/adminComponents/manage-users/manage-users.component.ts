//manage.users.component.ts

import { Component, OnInit } from '@angular/core';
import { UsersService } from '../../../services/users.service';
import { UserDTO } from '../../../dtos/user.model';
import { ToastrService } from 'ngx-toastr';

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
  public actionType: 'BAN' | 'UNBAN' | 'DELETE' | null = null;

  private pendingToastMessage: { type: 'success' | 'error', message: string, title: string } | null = null;



  constructor(
    private usersService: UsersService,
    protected toastr: ToastrService) {}

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
  confirmDelete(user: UserDTO): void {
    this.selectedUser = user;
    this.actionType = 'DELETE';
    this.modalTitle = 'Confirm Deletion';
    this.modalMessage = `Are you sure you want to permanently delete user "${user.username}"?`;
    this.showConfirmationModal = true;
  }

  onActionConfirmed(): void {
    if (!this.selectedUser || !this.actionType) return;

    const userId = this.selectedUser.id;
    let action$;


    switch (this.actionType) {
      case 'BAN':
        action$ = this.usersService.banUser(userId);
        break;
      case 'UNBAN':
        action$ = this.usersService.unbanUser(userId);
        break;
      case 'DELETE':
        action$ = this.usersService.deleteUser(userId);
        break;
      default:
        console.error(`Unknown action type: ${this.actionType}`);
        return;
    }

    action$.subscribe({
      next: () => {
        this.loadUsers();
        this.pendingToastMessage = {
          type: 'success',
          message:
            this.actionType === 'DELETE'
              ? 'User deleted successfully!'
              : this.actionType === 'BAN'
                ? 'User banned successfully!'
                : 'User unbanned successfully!',
          title: 'Success'
        };
        this.resetModalState();
      },
      error: (err) => {
        console.error(`Error performing ${this.actionType} action:`, err);
        this.pendingToastMessage = {
          type: 'error',
          message:
            this.actionType === 'DELETE'
              ? 'Error deleting user. Please try again.'
              : this.actionType === 'BAN'
                ? 'Error banning user. Please try again.'
                : 'Error unbanning user. Please try again.',
          title: 'Error'
        };
        this.resetModalState();
      }
    });

  }


  onModalFullyClosed(): void {
    if (this.pendingToastMessage) {
      const { type, message, title } = this.pendingToastMessage;
      type === 'success'
        ? this.toastr.success(message, title)
        : this.toastr.error(message, title);

      this.pendingToastMessage = null;
    }
  }


  resetModalState(): void {
    this.showConfirmationModal = false;
    this.modalTitle = '';
    this.modalMessage = '';
    this.selectedUser = null;
    this.actionType = null;
  }
}
