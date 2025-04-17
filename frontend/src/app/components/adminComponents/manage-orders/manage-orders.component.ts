import { Component, OnInit } from '@angular/core';
import { OrderDTO } from '../../../dtos/order.model';
import { OrderService } from '../../../services/order.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-admin-manage-orders',
  templateUrl: './manage-orders.component.html',
  styleUrls: ['./manage-orders.component.css']
})
export class AdminManageOrdersComponent implements OnInit {
  public orders: OrderDTO[] = [];
  public isLoading = true;

  public showConfirmationModal = false;
  public modalTitle = '';
  public modalMessage = '';
  public selectedOrder: OrderDTO | null = null;
  public actionType: 'DELETE' | 'CHANGE_STATUS' | null = null;

  public newStatus: string = '';
  public newTotalPrice: number = 0;
  public newOrderDate: string = '';

  private pendingToastMessage: { type: 'success' | 'error', message: string, title: string } | null = null;

  constructor(
    private orderService: OrderService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.isLoading = true;
    this.orderService.getAll().subscribe({
      next: (data) => {
        this.orders = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading orders:', err);
        this.isLoading = false;
      }
    });
  }

  confirmDelete(order: OrderDTO): void {
    this.selectedOrder = order;
    this.actionType = 'DELETE';
    this.modalTitle = 'Confirm Deletion';
    this.modalMessage = `Are you sure you want to delete order #${order.id}?`;
    this.showConfirmationModal = true;
  }

  confirmChangeStatus(order: OrderDTO): void {
    this.selectedOrder = order;
    this.actionType = 'CHANGE_STATUS';

    this.newStatus = order.status;
    this.newTotalPrice = order.totalPrice;
    this.newOrderDate = order.orderDate;

    this.modalTitle = 'Change Order Details';
    this.modalMessage = `Edit order #${order.id}:`;
    this.showConfirmationModal = true;
  }

  onActionConfirmed(): void {
    if (!this.selectedOrder || !this.actionType) return;

    const orderId = this.selectedOrder.id;

    let action$;

    switch (this.actionType) {
      case 'DELETE':
        action$ = this.orderService.delete(orderId);
        break;

      case 'CHANGE_STATUS':
        const updates = {
          status: this.newStatus,
          totalPrice: this.newTotalPrice,
          orderDate: this.newOrderDate
        };
        action$ = this.orderService.updateOrderFields(orderId, updates);
        break;

      default:
        return;
    }

    action$.subscribe({
      next: () => {
        this.loadOrders();
        this.pendingToastMessage = {
          type: 'success',
          message: this.actionType === 'DELETE'
            ? 'Order deleted successfully!'
            : 'Order updated successfully!',
          title: 'Success'
        };
        this.resetModalState();
      },
      error: (err) => {
        console.error(`Error performing ${this.actionType} action:`, err);
        this.pendingToastMessage = {
          type: 'error',
          message: this.actionType === 'DELETE'
            ? 'Error deleting order.'
            : 'Error updating order.',
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
    this.selectedOrder = null;
    this.actionType = null;
    this.newStatus = '';
    this.newTotalPrice = 0;
    this.newOrderDate = '';
  }
}
