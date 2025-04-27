import { Component, OnInit } from '@angular/core';
import { BookingService } from '../../../services/booking.service';
import { Booking } from '../../../dtos/booking.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-admin-manage-bookings',
  templateUrl: './manage-bookings.component.html',
  styleUrls: ['./manage-bookings.component.css']
})
export class AdminManageBookingsComponent implements OnInit {
  bookings: Booking[] = [];
  searchQuery: string = '';
  isLoading: boolean = false;


  constructor(
    private bookingService: BookingService,
    private toastr: ToastrService

  ) {}

  ngOnInit(): void {
    this.loadBookings();
  }

  loadBookings(): void {
    this.isLoading = true;

    this.bookingService.getAllBookings().subscribe({
      next: (data) => {
        this.bookings = data;
        this.isLoading = false;

        // Cargar usuario y restaurante para cada booking
        this.bookings.forEach(booking => {
          if (booking.userId) {
            this.bookingService.getUserById(booking.userId).subscribe({
              next: user => booking.user = user
            });
          }
        });
      },
      error: (err) => {
        console.error('Error loading bookings:', err);
        this.toastr.error('Error loading bookings', 'Error');
        this.isLoading = false;
      }
    });
  }

  searchBookings(): void {
    if (!this.searchQuery.trim()) {
      this.loadBookings();
      return;
    }

    this.bookingService.searchBookings(this.searchQuery.trim()).subscribe({
      next: (data) => {
        this.bookings = data;
      },
      error: (err) => {
        console.error('Error searching bookings:', err);
        this.toastr.error('Error searching bookings', 'Error');
      }
    });
  }

  cancelBooking(id: number): void {
    this.bookingService.deleteBooking(id).subscribe({
      next: () => {
        this.bookings = this.bookings.filter(b => b.id !== id);
        this.toastr.success('Booking deleted successfully', 'Deleted');
      },
      error: (err) => {
        console.error('Error deleting booking:', err);
        this.toastr.error('Error deleting booking', 'Error');
      }
    });
  }
  clearSearch(): void {
    this.searchQuery = '';
    this.loadBookings();
  }
}
