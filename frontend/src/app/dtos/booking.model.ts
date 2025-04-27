import { Restaurant } from './restaurant.model';
import { UserDTO } from './user.model';

export interface Booking {
  id?: number;
  restaurantId: number;
  userId: number;
  date: string;
  shift: string;
  numPeople: number;
  restaurantLocation?: string;

  // Nuevos campos rellenados en el momento de la reserva
  firstName?: string;
  lastName?: string;
  phone?: string;
  email?: string;

  // Información opcional extendida (si se desea mostrar detalles)
  restaurant?: {
    id: number;
    location: string;
  };

  user?: {
    id: number;
    username: string;
  };
}

