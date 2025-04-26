import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FAQ } from '../../viewsComponent/faqs/faqs.component';

@Component({
  selector: 'app-accordion-item',
  templateUrl: './accordion-item.component.html',
  styleUrls: ['./accordion-item.component.css']
})
export class AccordionItemComponent {
  @Input() faq!: FAQ;
  @Output() toggle = new EventEmitter<void>();
}
