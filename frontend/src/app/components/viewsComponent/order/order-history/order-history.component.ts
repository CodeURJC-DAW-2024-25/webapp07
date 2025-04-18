import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../../../services/order.service';
import { OrderDTO } from '../../../../dtos/order.model';


@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  styleUrls: ['./order-history.component.css']
})
export class OrderHistoryComponent implements OnInit {
  orders: OrderDTO[] = [];
  isLoading = true;

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.orderService.getHistory().subscribe({
      next: (data: OrderDTO[]) => {
        this.orders = data;
        this.isLoading = false;
      },
      error: (err: any) => {
        console.error('Error loading history:', err);
        this.isLoading = false;
      }
    });
  }
}
