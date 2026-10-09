import { Component, computed, input, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Acteur } from '../acteur.model';
import { RoleFilm } from '../film.model';

export interface SaisieRole {
  acteurId: number;
  personnage: string;
}

@Component({
  selector: 'app-film-acteurs',
  imports: [FormsModule, RouterLink],
  templateUrl: './film-acteurs.html',
})
export class FilmActeurs {
  acteurs = input.required<Acteur[]>();
  roles = input<RoleFilm[]>([]);
  disponibles = input.required<Acteur[]>();
  desactive = input(false);

  associer = output<SaisieRole>();
  dissocier = output<Acteur>();
  modifierRole = output<SaisieRole>();

  acteurSelectionne = signal<number | null>(null);
  personnage = signal('');
  enEdition = signal<number | null>(null);
  personnageEdite = signal('');

  private personnages = computed(() => new Map(this.roles().map(r => [r.acteurId, r.personnage])));

  personnageDe(acteur: Acteur) {
    return this.personnages().get(acteur.id) ?? '';
  }

  onAssocier() {
    const acteurId = this.acteurSelectionne();
    if (acteurId === null) {
      return;
    }
    this.associer.emit({ acteurId, personnage: this.personnage().trim() });
    this.acteurSelectionne.set(null);
    this.personnage.set('');
  }

  commencerEdition(acteur: Acteur) {
    this.enEdition.set(acteur.id);
    this.personnageEdite.set(this.personnageDe(acteur));
  }

  validerEdition(acteur: Acteur) {
    this.modifierRole.emit({ acteurId: acteur.id, personnage: this.personnageEdite().trim() });
    this.enEdition.set(null);
  }
}
