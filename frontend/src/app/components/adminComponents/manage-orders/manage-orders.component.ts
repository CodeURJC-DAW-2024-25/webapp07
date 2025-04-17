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

  public showEditPopup = false;
  public selectedOrder: OrderDTO | null = null;
  public newStatus: string = '';
  public newTotalPrice: number = 0;
  public newAddress: string = '';

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

  openEditPopup(order: OrderDTO): void {
    this.selectedOrder = order;
    this.newStatus = order.status;
    this.newTotalPrice = order.totalPrice;
    this.newAddress = order.address;
    this.showEditPopup = true;
  }

  cancelEditPopup(): void {
    this.showEditPopup = false;
    this.selectedOrder = null;
    this.newStatus = '';
    this.newTotalPrice = 0;
    this.newAddress = '';
  }

  confirmEditOrder(): void {
    if (!this.selectedOrder) return;

    const updates = {
      status: this.newStatus,
      totalPrice: this.newTotalPrice,
      address: this.newAddress
    };

    console.log('Order update payload:', updates);

    this.orderService.updateOrderFields(this.selectedOrder.id, updates).subscribe({
      next: () => {
        this.toastr.success('Order updated successfully!');
        this.loadOrders();
        this.cancelEditPopup();
      },
      error: () => {
        this.toastr.error('Error updating order.');
      }
    });
  }

  confirmDelete(order: OrderDTO): void {
    this.selectedOrder = order;
    if (!this.selectedOrder) return;

    this.orderService.delete(this.selectedOrder.id).subscribe({
      next: () => {
        this.toastr.success('Order deleted successfully!');
        this.loadOrders();
      },
      error: () => {
        this.toastr.error('Error deleting order.');
      }
    });
  }
}
