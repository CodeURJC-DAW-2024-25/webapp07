import { Component } from '@angular/core';

export interface Chef {
  name: string;
  specialty: string;
  image: string;
  socialLinks: {
    facebook?: string;
    twitter?: string;
    instagram?: string;
  };
}

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.css']
})
export class TeamComponent {
  chefs: Chef[] = [
    {
      name: 'Javier Llorente',
      specialty: 'Mediterranean Cuisine',
      image: 'assets/img/team-1.jpg',
      socialLinks: {
        facebook: '#',
        twitter: '#',
        instagram: '#'
      }
    },
    {
      name: 'William Carter',
      specialty: 'International and Fusion Cuisine',
      image: 'assets/img/team-2.jpg',
      socialLinks: {
        facebook: '#',
        twitter: '#',
        instagram: '#'
      }
    },
    {
      name: 'Miguel Gonzalez',
      specialty: 'Signature Chef and Fire Master',
      image: 'assets/img/team-3.jpg',
      socialLinks: {
        facebook: '#',
        twitter: '#',
        instagram: '#'
      }
    },
    {
      name: 'Émile Dupont',
      specialty: 'French Pastry',
      image: 'assets/img/team-4.jpg',
      socialLinks: {
        facebook: '#',
        twitter: '#',
        instagram: '#'
      }
    }
  ];

  animationDelays = ['0.1s', '0.3s', '0.5s', '0.7s'];
}
