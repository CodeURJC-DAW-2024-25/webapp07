import { Component, OnInit } from '@angular/core';
import { Restaurant } from '../../../dtos/restaurant.model';
import { RestaurantService } from '../../../services/restaurant.service';

@Component({
  selector: 'app-restaurant',
  templateUrl: './restaurant.component.html',
  styleUrls: ['./restaurant.component.css']
})

export class RestaurantComponent implements OnInit {

  restaurants: Restaurant[] = [];
  query: string = '';

  constructor(private restaurantService: RestaurantService) {}

  ngOnInit(): void {
    this.restaurantService.findAll().subscribe(data => {
      this.restaurants = data;
    });
  }

  searchRestaurants(query: string): void {
    this.restaurantService.searchByLocation(query).subscribe(data => {
      this.restaurants = data;
    });
  }
}
