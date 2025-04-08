import { Allergens } from './enums/allergens.enum';

export interface DishDTO {
  id: number;
  name: string;
  description: string;
  price: number;
  ingredients: string[];
  allergens: Allergens[];
  isVegan: boolean;
  dishImagePath: string;
  image: boolean;
  isAvailable: boolean;
  rates: number[];
  rate: number;
}


export interface AddDishRequest{
  name: string;
  price: number;
  allergens: Allergens[];
}
