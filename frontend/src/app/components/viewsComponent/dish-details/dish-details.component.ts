import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DishService } from '../../../services/dish.service';
import { DishDTO } from '../../../dtos/dish.model';

@Component({
  selector: 'app-dish-details',
  templateUrl: './dish-details.component.html',
  styleUrls: ['./dish-details.component.css']
})
export class DishDetailsComponent implements OnInit {
  dish!: DishDTO;
  stars: number[] = [];
  noStars: number[] = [];

  constructor(
    private route: ActivatedRoute,
    private dishService: DishService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.dishService.getDishById(id).subscribe({
      next: (data) => {
        this.dish = data;
        const roundedRate = Math.round(this.dish.rate);
        this.stars = Array(roundedRate).fill(0);
        this.noStars = Array(5 - roundedRate).fill(0);
      },
      error: (err) => console.error('Error cargando el plato:', err)
    });
  }
}
