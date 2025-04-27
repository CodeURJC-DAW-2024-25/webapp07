import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from '../../../../services/order.service';

@Component({
  selector: 'app-order-info',
  templateUrl: './order-info.component.html',
  styleUrls: ['./order-info.component.css']
})
export class OrderInfoComponent implements OnInit {
  order: any = null;
  isLoading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (isNaN(id)) {
      this.error = 'Invalid order ID';
      this.isLoading = false;
      return;
    }
    this.orderService.getSummary(id).subscribe({
      next: data => {
        this.order = data;
        this.isLoading = false;
      },
      error: err => {
        this.error = err.status === 409
          ? 'Order has already been paid.'
          : 'Error loading order info';
        this.isLoading = false;
      }
    });
  }

  goBack() {
    this.router.navigate(['/orders/history']);
  }

  downloadPdf(orderId: number) {
    this.orderService.downloadInvoice(orderId).subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `invoice_${orderId}.pdf`;
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }
}
