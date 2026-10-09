import { Component, computed, inject, signal } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { catchError, of, switchMap } from 'rxjs';
import { RouterLink } from '@angular/router';
import { ActeurService, SensTri, TriActeur } from '../acteur.service';

@Component({
  selector: 'app-acteur-list',
  imports: [RouterLink],
  templateUrl: './acteur-list.html',
})
export class ActeurList {
  private service = inject(ActeurService);
  readonly taille = 5;
  page = signal(0);
  tri = signal<TriActeur>('nom');
  sens = signal<SensTri>('asc');
  erreur = signal('');

  resultat = toSignal(
    toObservable(computed(() => ({ page: this.page(), tri: this.tri(), sens: this.sens() }))).pipe(
      switchMap(({ page, tri, sens }) => {
        this.erreur.set('');
        return this.service.getPage(page, this.taille, tri, sens).pipe(
          catchError(() => {
            this.erreur.set('Impossible de charger les acteurs. Vérifie que le backend est démarré.');
            return of(null);
          }),
        );
      }),
    ),
  );

  trierPar(colonne: TriActeur) {
    if (this.tri() === colonne) {
      this.sens.update(s => (s === 'asc' ? 'desc' : 'asc'));
    } else {
      this.tri.set(colonne);
      this.sens.set('asc');
    }
    this.page.set(0);
  }

  indicateur(colonne: TriActeur) {
    if (this.tri() !== colonne) {
      return '';
    }
    return this.sens() === 'asc' ? ' ▲' : ' ▼';
  }
}
