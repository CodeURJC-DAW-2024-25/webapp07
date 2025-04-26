import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DishService } from '../../../services/dish.service';

@Component({
  selector: 'app-dish-rating',
  templateUrl: './dish-rating.component.html',
  styleUrls: ['./dish-rating.component.css']
})
export class DishRatingComponent {
  selectedStars = 0;
  dishId!: number;

  constructor(
    private route: ActivatedRoute,
    private dishService: DishService,
    private router: Router
  ) {}

  ngOnInit() {
    this.dishId = Number(this.route.snapshot.paramMap.get('id'));
  }

  selectStars(stars: number) {
    this.selectedStars = stars;
  }

  submitRating() {
    if (this.selectedStars > 0) {
      this.dishService.rateDish(this.dishId, this.selectedStars).subscribe({
        next: () => {
          alert('Thank you for your rating!');
          this.router.navigate(['/dishes', this.dishId]);
        },
        error: () => {
          alert('Error submitting rating.');
        }
      });
    } else {
      alert('Please select a rating!');
    }
  }
}
