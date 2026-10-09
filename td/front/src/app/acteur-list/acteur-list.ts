import { Component, inject, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { catchError, of } from 'rxjs';
import { ActeurService } from '../acteur.service';

@Component({
  selector: 'app-acteur-list',
  templateUrl: './acteur-list.html',
})
export class ActeurList {
  private service = inject(ActeurService);
  erreur = signal('');
  acteurs = toSignal(
    this.service.getAll().pipe(
      catchError(() => {
        this.erreur.set('Impossible de charger les acteurs. Vérifie que le backend est démarré.');
        return of([]);
      }),
    ),
  );
}
