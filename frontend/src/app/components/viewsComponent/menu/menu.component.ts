import { Component, OnInit } from '@angular/core';
import { DishService } from '../../../services/dish.service';
import { DishDTO } from '../../../dtos/dish.model';
import { BehaviorSubject } from 'rxjs';
import { AuthService } from '../../../services/auth.service';
import {OrderService} from "../../../services/order.service";
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
})
export class MenuComponent implements OnInit {
  dishData: DishDTO[] = [];
  currentPage = 0;
  pageSize = 10;
  hasMoreData = true;
  isLoggedIn$ = this.authService.isAuthenticated$;

  constructor(private dishService: DishService, private orderService: OrderService, private authService: AuthService, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.loadMoreDishes();
  }

  loadMoreDishes(): void {
    this.dishService.getDishes(this.currentPage, this.pageSize).subscribe((data: DishDTO[]) => {
      if (data.length < this.pageSize) {
        this.hasMoreData = false;
      }
      this.dishData = [...this.dishData, ...data];
      this.currentPage++;
    });
  }




  addToCart(dishId: number): void {
    this.orderService.addToCart(dishId).subscribe({
      next: () => {
        this.toastr.success('Dish added to cart!');
      },
      error: (err) => {
        console.error('Error adding dish to cart:', err);
        this.toastr.error('Could not add dish to cart.');
      }
    });
  }

}
