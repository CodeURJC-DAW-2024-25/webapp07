import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from '../../../../services/order.service';

@Component({
  selector: 'app-confirm-payment',
  templateUrl: './confirm-payment.component.html',
  styleUrls: ['./confirm-payment.component.css']
})
export class ConfirmPaymentComponent implements OnInit {
  orderId!: number;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      if (!isNaN(id)) {
        this.orderId = id;
      }
    });
  }


  confirmPayment(): void {
    this.orderService.updateStatus(this.orderId, 'Paid').subscribe({
      next: () => {
        this.router.navigate(['/orders/history']);
      },
      error: (err) => {
        console.error('Error confirming payment:', err);
      }
    });
  }


  cancel(): void {
    this.router.navigate(['/dishes']);
  }
}
