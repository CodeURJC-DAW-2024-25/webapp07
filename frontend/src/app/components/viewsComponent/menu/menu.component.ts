import { Component, OnInit } from '@angular/core';
import { DishService } from '../../../services/dish.service';
import { DishDTO } from '../../../dtos/dish.model';
import {AuthService} from "../../../services/auth.service";
import { OrderService } from '../../../services/order.service';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {
  dishes: DishDTO[] = [];
  isLoading = true;
  isLoggedIn$ = this.authService.isAuthenticated$;
  isAdmin$ = this.authService.isAdmin$;

  constructor(
    private dishService: DishService,
    private authService: AuthService,
    private orderService: OrderService,
    private toastr: ToastrService
  ) {}


  ngOnInit(): void {
    this.dishService.getDishes().subscribe({
      next: (data: DishDTO[]) => {
        console.log("DATA= " + data);
        this.dishes = data
        this.isLoading = false;
      },
      error: (err) => console.error('Error cargando platos:', err)
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
