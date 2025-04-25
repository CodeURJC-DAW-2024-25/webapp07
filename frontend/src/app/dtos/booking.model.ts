import { Restaurant } from './restaurant.model';
import { UserDTO } from './user.model';

export interface Booking {
  id: number;               // Opcional porque se genera en el backend
  restaurantId: number;
  userId: number;
  date: string;              // Se suele usar string tipo "2025-04-12"
  shift: string;             // "LUNCH" o "DINNER"
  numPeople: number;
  restaurant?: Restaurant; // <
  user?: UserDTO;

}
