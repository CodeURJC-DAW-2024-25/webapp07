import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-counter',
  template: `{{ currentValue }}`,
  styleUrls: ['./counter.component.css']
})
export class CounterComponent implements OnInit {
  @Input() target: number = 0;
  @Input() delay: number = 10;
  @Input() duration: number = 2000;

  currentValue: number = 0;

  ngOnInit() {
    this.animateCounter();
  }

  animateCounter() {
    const increment = this.target / (this.duration / this.delay);

    const timer = setInterval(() => {
      this.currentValue += increment;
      if (this.currentValue >= this.target) {
        this.currentValue = this.target;
        clearInterval(timer);
      }
    }, this.delay);
  }
}
