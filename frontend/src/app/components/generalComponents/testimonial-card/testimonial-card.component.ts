import { Component, Input } from '@angular/core';
import { Testimonial } from '../../viewsComponent/faqs/faqs.component';

@Component({
  selector: 'app-testimonial-card',
  templateUrl: './testimonial-card.component.html',
  styleUrls: ['./testimonial-card.component.css']
})
export class TestimonialCardComponent {
  @Input() testimonial!: Testimonial;
}
