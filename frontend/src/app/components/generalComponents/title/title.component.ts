// title.component.ts
import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router';
import { TitleService } from '../../../services/title.service';
import { filter, map } from 'rxjs/operators';

@Component({
  selector: 'app-title',
  templateUrl: './title.component.html',
  styleUrls: ['./title.component.css']
})
export class TitleComponent implements OnInit {
  pageTitle: string = '';
  isVisible: boolean = true;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private titleService: TitleService
  ) {}

  ngOnInit() {
    // Config from routes
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      map(() => {
        let route = this.activatedRoute;
        while (route.firstChild) route = route.firstChild;
        return route;
      }),
      filter(route => route.outlet === 'primary')
    ).subscribe(route => {
      const data = route.snapshot.data;
      if (data['title']) {
        this.pageTitle = data['title'];
      }
      if (data['showTitle'] !== undefined) {
        this.isVisible = data['showTitle'];
      }
    });

    // Config from service
    this.titleService.title$.subscribe(({title, visible}) => {
      if (title !== undefined) {
        this.pageTitle = title;
      }
      if (visible !== undefined) {
        this.isVisible = visible;
      }
    });
  }
}
