import { Component, inject, input, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { catchError, forkJoin, of, switchMap } from 'rxjs';
import { ActeurService } from '../acteur.service';

@Component({
  selector: 'app-acteur-detail',
  imports: [DatePipe, RouterLink],
  templateUrl: './acteur-detail.html',
})
export class ActeurDetail {
  private service = inject(ActeurService);

  id = input.required<string>();
  erreur = signal('');

  donnees = toSignal(
    toObservable(this.id).pipe(
      switchMap(id => {
        this.erreur.set('');
        return forkJoin({
          acteur: this.service.getById(Number(id)),
          films: this.service.getFilms(Number(id)),
        }).pipe(
          catchError((err: HttpErrorResponse) => {
            this.erreur.set(
              err.status === 404
                ? 'Cet acteur n’existe pas.'
                : 'Impossible de charger l’acteur. Vérifie que le backend est démarré.',
            );
            return of(null);
          }),
        );
      }),
    ),
  );
}
