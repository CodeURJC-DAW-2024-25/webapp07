import { Component, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-dish-filter',
  templateUrl: './dish-filter.component.html'
})
export class DishFilterComponent {
  name: string = '';
  maxPrice: number | null = null;
  ingredient: string = '';

  @Output() filtersChanged = new EventEmitter<any>();

  applyFilters() {
    this.filtersChanged.emit({
      name: this.name,
      maxPrice: this.maxPrice,
      ingredient: this.ingredient
    });
  }
}
