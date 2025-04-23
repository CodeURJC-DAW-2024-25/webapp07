import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OrderService } from '../../../../services/order.service';
import { OrderDTO } from '../../../../dtos/order.model';

@Component({
  selector: 'app-order-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.css']
})
export class OrderSummaryComponent implements OnInit {
  order: OrderDTO | null = null;
  isLoading = true;

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    const orderId = Number(this.route.snapshot.paramMap.get('id'));
    if (!isNaN(orderId)) {
      this.orderService.getSummary(orderId).subscribe({
        next: (data) => {
          this.order = data;
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error fetching summary:', err);
          this.isLoading = false;
        }
      });
    }
  }

  confirmOrder(): void {
    if (this.order && this.order.id) {
      const updates = {
        status: 'Accepted',
        address: this.order.address
      };

      this.orderService.updateOrderFields(this.order.id, updates).subscribe({
        next: () => {
          console.log('Order updated successfully');
        },
        error: (err) => {
          console.error('Error updating order:', err);
        }
      });
    }
  }
}
