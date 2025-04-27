import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { DishService } from '../../../services/dish.service';
import { OrderService } from '../../../services/order.service';
import { DishDTO } from '../../../dtos/dish.model';
import {AuthService} from "../../../services/auth.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-dish-details',
  templateUrl: './dish-details.component.html',
  styleUrls: ['./dish-details.component.css']
})
export class DishDetailsComponent implements OnInit {
  dish!: DishDTO;
  stars: number[] = [];
  noStars: number[] = [];
  id!: number;
  isLoggedIn$ = this.authService.isAuthenticated$;
  isAdmin$ = this.authService.isAdmin$;

  constructor(
    private route: ActivatedRoute,
    private dishService: DishService,
    private orderService: OrderService,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService

  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.dishService.getDishById(id).subscribe({
      next: (data) => {
        this.dish = data;
        console.log(this.dish);
        const roundedRate = Math.round(this.dish.rate);
        this.stars = Array(roundedRate).fill(0);
        this.noStars = Array(5 - roundedRate).fill(0);
      },
      error: (err) => console.error('Error cargando el plato:', err)
    });
  }

  toggleAvailability(): void {
    const isCurrentlyAvailable = this.dish.available;

    const patchCall = isCurrentlyAvailable
      ? this.dishService.disableDish(this.dish.id)
      : this.dishService.enableDish(this.dish.id);

    patchCall.subscribe({
      next: () => {
        this.dish.available = !isCurrentlyAvailable;
        this.router.navigate(['/dishes']);
        console.log(`Plato ${isCurrentlyAvailable ? 'deshabilitado' : 'habilitado'}`);
      },
      error: err => console.error('Error al cambiar la disponibilidad:', err)
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
