import { DishDTO } from './dish.model';
import { UserDTO } from './user.model';

export interface OrderDTO {
  id: number;
  dishes: DishDTO[];
  user: UserDTO;
  orderDate: string;
  address: string;
  status: string;
  totalPrice: number;
}
