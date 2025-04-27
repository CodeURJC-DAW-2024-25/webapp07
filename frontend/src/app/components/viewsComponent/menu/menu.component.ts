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
  isLoading = false;
  isLoggedIn$ = this.authService.isAuthenticated$;
  isAdmin$ = this.authService.isAdmin$;
  filters = {
    name: '',
    maxPrice: undefined,
    ingredient: ''
  };

  constructor(private dishService: DishService, private orderService: OrderService, private authService: AuthService, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.loadMoreDishes();
  }

  loadMoreDishes(): void {
    this.isLoading = true;


    this.dishService.getDishes(this.currentPage, this.pageSize, this.filters).subscribe((data: DishDTO[]) => {
      this.isLoading = false;

      const availableDishes = data.filter(dish => dish.available);

      if (availableDishes.length < this.pageSize) {
        this.hasMoreData = false;
      }
      this.dishData = [...this.dishData, ...availableDishes];
      this.currentPage++;
    });
  }
  onFiltersChanged(filters: any): void {
    this.filters = filters;
    this.currentPage = 0;
    this.dishData = [];
    this.hasMoreData = true;
    this.loadMoreDishes();
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
