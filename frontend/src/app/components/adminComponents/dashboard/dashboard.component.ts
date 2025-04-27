import { Component, OnInit } from '@angular/core';
import { DishService } from '../../../services/dish.service';
import { DishDTO } from '../../../dtos/dish.model';
import { Chart, CategoryScale, LinearScale, BarElement, BarController, Title, Tooltip, Legend } from 'chart.js';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  topDishes: DishDTO[] = [];

  constructor(private dishService: DishService) {
    Chart.register(CategoryScale, LinearScale, BarElement, BarController, Title, Tooltip, Legend);
  }


  ngOnInit(): void {
    this.dishService.getTop5Dishes().subscribe((dishes: DishDTO[]) => {
      console.log(dishes);
      this.topDishes = dishes;

      const dishNames = this.topDishes.map(dish => dish.name);
      const ratingsToPrices = this.topDishes.map(dish => {
        if (dish.rates && dish.rates.length > 0 && dish.price) {
          const sumRatings = dish.rates.reduce((sum, current) => sum + current, 0);
          const avgRating = sumRatings / dish.rates.length;
          const ratio = (avgRating / dish.price) * 100;
          return Math.round(ratio * 100) / 100;
        }
        return 0;
      });

      console.log(dishNames);
      console.log(ratingsToPrices);

      this.createChart(dishNames, ratingsToPrices);
    });
  }

  createChart(labels: string[], data: number[]): void {
    const ctx = document.getElementById('topDishesChart') as HTMLCanvasElement;

    new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'Rating/Price (%)',
          data: data,
          backgroundColor: 'rgba(75, 192, 192, 0.5)',
          borderColor: 'rgba(75, 192, 192, 1)',
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        scales: {
          x: {
            type: 'category',
            title: {
              display: true,
              text: 'Dish Name',
              color: '#FEA116'
            }
          },
          y: {
            type: 'linear',
            beginAtZero: true,
            title: {
              display: true,
              text: 'Rating to Price Ratio (%)',
              color: '#FEA116'
            }
          }
        }
      }
    });
  }
}
