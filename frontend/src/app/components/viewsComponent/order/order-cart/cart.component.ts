import { Component, OnInit } from '@angular/core';
import { OrderDTO } from '../../../../dtos/order.model';
import { OrderService } from '../../../../services/order.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cart: OrderDTO | null = null;
  isLoading = true;

  constructor(private orderService: OrderService) {}

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
}
