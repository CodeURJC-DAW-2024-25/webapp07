import { Component, OnInit } from '@angular/core';
import { OrderDTO } from '../../../../dtos/order.model';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from '../../../../services/order.service';

@Component({
  selector: 'app-order-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.css']
})
export class OrderSummaryComponent implements OnInit {
  order: OrderDTO | null = null;
  isLoading = true;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    const nav = this.router.getCurrentNavigation();
    const state = nav?.extras?.state as { order: OrderDTO };

    if (state?.order) {
      this.order = state.order;
      this.isLoading = false;
    } else {
      console.error('No order data found in navigation state.');
      this.isLoading = false;
    }
  }

  confirmOrder(): void {
    if (!this.order) return;

    const updates = {
      status: 'Accepted',
      address: this.order.address
    };

    this.orderService.updateOrderFields(this.order.id, updates).subscribe({
      next: () => {
        alert('Order confirmed!');
      },
      error: (err) => {
        console.error('Error updating order:', err);
      }
    });
  }
}
