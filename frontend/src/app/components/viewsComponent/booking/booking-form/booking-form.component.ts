import { Component, OnInit } from '@angular/core';
import { BookingService } from '../../../../services/booking.service';
import { Booking } from '../../../../dtos/booking.model';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../../../services/auth.service';
import { Router } from '@angular/router';
import { UserDTO } from '../../../../dtos/user.model';

@Component({
  selector: 'app-booking-form',
  templateUrl: './booking-form.component.html',
  styleUrls: ['./booking-form.component.css']
})
export class BookingFormComponent implements OnInit {

  booking: any = {
    restaurantId: null,
    date: '',
    shift: '',
    numPeople: null,
    userId: null,
    firstName: '',
    lastName: '',
    email: '',
    phone: ''
  };

  restaurants: any[] = [];
  availableSeatsArray: number[] = [];
  user: UserDTO | null = null;
  hasActiveBooking: boolean = false;
  checkingActiveBooking: boolean = true;


  constructor(
    private bookingService: BookingService,
    private authService: AuthService,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Usamos el observable y comprobamos con una condición más robusta
    this.authService.userData$.subscribe({
      next: (user) => {
        // solo redirige si ya se ha intentado cargar el usuario (¡no antes!)
        if (user) {
          this.user = user;
          this.booking.userId = user.id;
          this.booking.firstName = user.firstName || '';
          this.booking.lastName = user.lastName || '';
          this.booking.email = user.email || '';
          this.booking.phone = user.phoneNumber || '';

          //Verificar si ya tiene una reserva activa
          this.checkActiveBooking();
        } else if (!this.authService.currentAuthStatus) {
          // Si el usuario no está logueado realmente, redirige
          this.router.navigate(['/login']);
        }
      }
    });

    this.loadRestaurants();
  }

  checkActiveBooking(): void {
    this.bookingService.getMyActiveBooking().subscribe({
      next: (activeBooking: Booking) => {
        console.log("El usuario ya tiene una reserva activa:", activeBooking);
        this.hasActiveBooking = true;
        this.router.navigate(['/booking/existing']);
      },
      error: (err) => {
        if (err.status === 404) {
          console.log("No hay reservas activas, se puede continuar.");
          this.hasActiveBooking = false;
          this.checkingActiveBooking = false;  // ✅ Ya hemos terminado de comprobar
        } else {
          console.error("Error al comprobar la reserva activa:", err);
          this.checkingActiveBooking = false;  // incluso en error, termina
        }
      }
    });
  }


  loadRestaurants(): void {
    this.http.get<any[]>('/api/v1/restaurants').subscribe({
      next: (data) => {
        this.restaurants = data;
      },
      error: (err) => {
        console.error("Error cargando restaurantes:", err);
      }
    });
  }

  updateAvailableSeats(): void {
    const { restaurantId, date, shift } = this.booking;
    console.log('updateAvailableSeats triggered:', { restaurantId, date, shift });

    if (restaurantId && date && shift) {
      const formattedDate = new Date(date).toISOString().split('T')[0];
      this.bookingService.getAvailableSeats(restaurantId, date, shift).subscribe({
        next: (seats: number) => {
          console.log('Available seats:', seats);
          this.availableSeatsArray = Array.from({ length: seats }, (_, i) => i + 1);
        },
        error: (err) => {
          console.error("Error obteniendo asientos disponibles:", err);
          this.availableSeatsArray = [];
        }
      });
    }
  }


  submitBooking(): void {
    this.bookingService.createBooking(this.booking).subscribe({
      next: (data: Booking) => {
        console.log("Reserva creada con éxito:", data);
        this.router.navigate(['/booking/confirmation']);
      },
      error: (err: any) => {
        console.error("Error al crear la reserva:", err);
      }
    });
  }
}
