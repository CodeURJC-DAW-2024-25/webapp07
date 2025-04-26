// src/app/components/viewsComponent/booking/booking-existing/booking-existing.component.ts

import { Component } from '@angular/core';

@Component({
  selector: 'app-booking-existing',
  templateUrl: './booking-existing.component.html',
  styleUrls: ['./booking-messages.component.css']
})
export class BookingExistingComponent {
  message: string = 'You already have an active reservation. To make a new one, please cancel your existing booking in your profile.';
}
