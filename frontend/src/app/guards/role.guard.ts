// src/app/guards/role.guard.ts
import { inject } from '@angular/core';
import {
  CanActivateFn,
  Router,
  ActivatedRouteSnapshot,
  UrlTree
} from '@angular/router';
import { take, map } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot
): import("rxjs").Observable<boolean|UrlTree> =>
{
  const auth   = inject(AuthService);
  const router = inject(Router);
  const allowedRoles = route.data['roles'] as string[] || [];

  return auth.userData$.pipe(
    take(1),
    map(user =>
      user?.roles?.some(r => allowedRoles.includes(r))
        ? true
        : router.createUrlTree(['/unauthorized'])
    )
  );
};
