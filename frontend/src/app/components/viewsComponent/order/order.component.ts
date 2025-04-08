import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../../services/order.service';
import { OrderDTO } from '../../../dtos/order.model';
import {Router} from "@angular/router";


@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit {
  orders: OrderDTO[] = [];
  displayedColumns: string[] = ['id', 'user', 'date', 'status', 'totalPrice', 'actions'];

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.orderService.getAll().subscribe({
      next: (orders) => this.orders = orders,
      error: (err) => console.error('Error loading orders:', err)
    });
  }
}
