import { Component, OnInit } from '@angular/core';
import { OrderDTO } from '../../../../dtos/order.model';
import { OrderService } from '../../../../services/order.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cart: OrderDTO | null = null;
  isLoading = true;

  constructor(private orderService: OrderService, private router: Router) {}

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): void {
    this.orderService.viewCart().subscribe({
      next: (data) => {
        this.cart = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading cart:', err);
        this.isLoading = false;
      }
    });
  }
  placeOrder(): void {
    if (this.cart && this.cart.id) {
      const updates = {
        status: 'Accepted'
      };

      this.orderService.updateOrderFields(this.cart.id, updates).subscribe({
        next: () => {
          this.router.navigate(['/orders/summary'], {
            state: { order: this.cart }
          });
        },
        error: (err) => console.error('Error placing order:', err)
      });
    }
  }
}
