import { Component, Input } from '@angular/core';
import { Chef } from '../team/team.component';

@Component({
  selector: 'app-chef-card',
  templateUrl: './chef-card.component.html',
  styleUrls: ['./chef-card.component.css']
})
export class ChefCardComponent {
  @Input() chef!: Chef;
  @Input() animationDelay: string = '0s';
}
