import { Component, OnInit } from '@angular/core';
import { DishService } from '../../../services/dish.service';
import { DishDTO } from '../../../dtos/dish.model';
import {AuthService} from "../../../services/auth.service";

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

  constructor(private dishService: DishService,
              private authService: AuthService) {}

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
}
