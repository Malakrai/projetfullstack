import { Component, computed, DestroyRef, inject, input, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { takeUntilDestroyed, toObservable, toSignal } from '@angular/core/rxjs-interop';
import { catchError, Observable, of, switchMap } from 'rxjs';
import { FilmService } from '../film.service';
import { ActeurService } from '../acteur.service';
import { Acteur } from '../acteur.model';
import { FilmActeurs, SaisieRole } from '../film-acteurs/film-acteurs';

@Component({
  selector: 'app-film-detail',
  imports: [DatePipe, FilmActeurs, RouterLink],
  templateUrl: './film-detail.html',
})
export class FilmDetail {
  private service = inject(FilmService);
  private acteurService = inject(ActeurService);
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

  private tousLesActeurs = toSignal(
    this.acteurService.getAll().pipe(
      catchError(() => {
        this.erreurAction.set('Impossible de charger la liste des acteurs.');
        return of([] as Acteur[]);
      }),
    ),
    { initialValue: [] as Acteur[] },
  );

  acteursDisponibles = computed(() => {
    const associes = this.film()?.acteurs?.map(a => a.id) ?? [];
    return this.tousLesActeurs().filter(a => !associes.includes(a.id));
  });

  recharger() {
    this.rechargement.update(n => n + 1);
  }

  associer({ acteurId, personnage }: SaisieRole) {
    this.executer(this.service.associerActeur(this.filmId(), acteurId, personnage), 'Association impossible.', () =>
      this.recharger(),
    );
  }

  modifierRole({ acteurId, personnage }: SaisieRole) {
    this.executer(this.service.modifierRole(this.filmId(), acteurId, personnage), 'Modification du rôle impossible.', () =>
      this.recharger(),
    );
  }

  dissocier(acteur: Acteur) {
    this.executer(this.service.dissocierActeur(this.filmId(), acteur.id), 'Dissociation impossible.', () =>
      this.recharger(),
    );
  }

  onSupprimer() {
    const film = this.film();
    if (!film || !window.confirm(`Supprimer le film « ${film.titre} » ?`)) {
      return;
    }
    this.executer(this.service.supprimer(film.id), 'Suppression impossible.', () =>
      this.router.navigate(['/films']),
    );
  }

  private executer(requete: Observable<void>, messageErreur: string, suite: () => void) {
    if (this.actionEnCours()) {
      return;
    }
    this.erreurAction.set('');
    this.actionEnCours.set(true);
    requete.pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: () => {
        this.actionEnCours.set(false);
        suite();
      },
      error: () => {
        this.erreurAction.set(messageErreur);
        this.actionEnCours.set(false);
      },
    });
  }
}
