// src/app/guards/auth.guard.ts
import { inject } from '@angular/core';
import {
  CanActivateFn,
  Router,
  UrlTree
} from '@angular/router';
import { take, map } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (_, __):
  import("rxjs").Observable<boolean|UrlTree> =>
{
  const auth   = inject(AuthService);
  const router = inject(Router);

  return auth.isAuthenticated$.pipe(
    take(1),
    map(logged =>
      logged
        ? true
        : router.createUrlTree(['/login'])
    )
  );
};
