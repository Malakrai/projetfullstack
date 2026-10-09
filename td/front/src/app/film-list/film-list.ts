import { Component, computed, DestroyRef, inject, signal } from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { catchError, of } from 'rxjs';
import { FilmService } from '../film.service';
import { Film } from '../film.model';
import { FilmCard } from '../film-card/film-card';

@Component({
  selector: 'app-film-list',
  imports: [FilmCard],
  templateUrl: './film-list.html',
})
export class FilmList {
  private service = inject(FilmService);
  private destroyRef = inject(DestroyRef);
  private idsSupprimes = signal<number[]>([]);
  suppressionEnCours = signal(false);
  erreurSuppression = signal('');
  erreur = signal('');
  private filmsApi = toSignal(
    this.service.getAll().pipe(
      catchError(() => {
        this.erreur.set('Impossible de charger les films. Vérifie que le backend est démarré.');
        return of([]);
      }),
    ),
  );
  films = computed(() =>
    this.filmsApi()?.filter(film => !this.idsSupprimes().includes(film.id)),
  );

  onSupprimer(film: Film) {
    if (this.suppressionEnCours() || !window.confirm(`Supprimer le film « ${film.titre} » ?`)) {
      return;
    }

    this.erreurSuppression.set('');
    this.suppressionEnCours.set(true);
    this.service.supprimer(film.id).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: () => {
        this.idsSupprimes.update(ids => [...ids, film.id]);
        this.suppressionEnCours.set(false);
      },
      error: () => {
        this.erreurSuppression.set('Suppression impossible. Le film est conservé dans la liste.');
        this.suppressionEnCours.set(false);
      },
    });
  }
}
