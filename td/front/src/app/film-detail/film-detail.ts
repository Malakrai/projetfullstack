import { Component, computed, DestroyRef, inject, input, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { takeUntilDestroyed, toObservable, toSignal } from '@angular/core/rxjs-interop';
import { catchError, of, switchMap } from 'rxjs';
import { FilmService } from '../film.service';

@Component({
  selector: 'app-film-detail',
  imports: [DatePipe, RouterLink],
  templateUrl: './film-detail.html',
})
export class FilmDetail {
  private service = inject(FilmService);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);

  id = input.required<string>();
  filmId = computed(() => Number(this.id()));
  private rechargement = signal(0);
  erreur = signal('');
  erreurAction = signal('');
  actionEnCours = signal(false);

  film = toSignal(
    toObservable(computed(() => ({ id: this.filmId(), n: this.rechargement() }))).pipe(
      switchMap(({ id }) => {
        this.erreur.set('');
        return this.service.getById(id).pipe(
          catchError((err: HttpErrorResponse) => {
            this.erreur.set(
              err.status === 404
                ? 'Ce film n’existe pas.'
                : 'Impossible de charger le film. Vérifie que le backend est démarré.',
            );
            return of(null);
          }),
        );
      }),
    ),
  );

  recharger() {
    this.rechargement.update(n => n + 1);
  }

  onSupprimer() {
    const film = this.film();
    if (!film || this.actionEnCours() || !window.confirm(`Supprimer le film « ${film.titre} » ?`)) {
      return;
    }

    this.erreurAction.set('');
    this.actionEnCours.set(true);
    this.service.supprimer(film.id).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: () => this.router.navigate(['/films']),
      error: () => {
        this.erreurAction.set('Suppression impossible.');
        this.actionEnCours.set(false);
      },
    });
  }
}
