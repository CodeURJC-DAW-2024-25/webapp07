import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from '../../../../services/order.service';
import { DishDTO } from '../../../../dtos/dish.model';

interface OrderSummary {
  id: number;
  dishes: DishDTO[];
  totalPrice: number;
  deliveryCost: number;
  finalPrice: number;
  address: string;
  user: {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
  };
  status: 'Paid' | 'Accepted' | 'Pending' | string;
}

@Component({
  selector: 'app-order-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.css']
})
export class OrderSummaryComponent implements OnInit {
  order: OrderSummary | null = null;
  originalAddress = '';
  isLoading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    const orderId = Number(this.route.snapshot.paramMap.get('id'));
    if (isNaN(orderId)) {
      this.error = 'Invalid order ID';
      this.isLoading = false;
      return;
    }

    this.orderService.getSummary(orderId).subscribe({
      next: (data: OrderSummary) => {
        // Si ya está pagada, redirigimos a la vista de info
        if (data.status === 'Paid') {
          this.router.navigate(['/orders', orderId, 'info']);
          return;
        }

        // Mostramos el resumen para confirmar
        this.order = data;

        // Inicializamos originalAddress y dejamos address no nulo
        this.originalAddress = this.order.address ?? '';
        this.order.address = this.originalAddress;

        this.isLoading = false;
      },
      error: err => {
        console.error('Error fetching summary:', err);
        this.error = 'Error loading order summary';
        this.isLoading = false;
      }
    });
  }

  confirmOrder(): void {
    if (!this.order) {
      return;
    }

    const addr = this.order.address?.trim();
    if (!addr) {
      this.error = 'Please provide a valid shipping address.';
      return;
    }

    const updates = {
      status: 'Accepted',
      address: addr
    };

    this.orderService.updateOrderFields(this.order.id, updates).subscribe({
      next: () => {
        this.router.navigate(['/orders', this.order!.id, 'confirm-payment']);
      },
      error: err => {
        console.error('Error confirming order:', err);
        this.error = 'Could not confirm order';
      }
    });
  }

}
