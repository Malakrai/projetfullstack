import { Component, computed, DestroyRef, inject, input, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FilmSaisie, FilmService } from '../film.service';
import { Genre, GENRES } from '../film.model';

@Component({
  selector: 'app-film-form',
  imports: [FormsModule, RouterLink],
  templateUrl: './film-form.html',
})
export class FilmForm implements OnInit {
  private service = inject(FilmService);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);

  id = input<string>();
  enEdition = computed(() => this.id() !== undefined);
  genres = GENRES;

  titre = signal('');
  realisateur = signal('');
  dateSortie = signal('');
  genre = signal<Genre | null>(null);

  chargement = signal(false);
  envoiEnCours = signal(false);
  erreur = signal('');

  ngOnInit() {
    const id = this.id();
    if (id === undefined) {
      return;
    }

    this.chargement.set(true);
    this.service.getById(Number(id)).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: film => {
        this.titre.set(film.titre);
        this.realisateur.set(film.realisateur ?? '');
        this.dateSortie.set(film.dateSortie ?? '');
        this.genre.set(film.genre);
        this.chargement.set(false);
      },
      error: () => {
        this.erreur.set('Impossible de charger le film à modifier.');
        this.chargement.set(false);
      },
    });
  }

  enregistrer() {
    if (!this.titre().trim() || this.envoiEnCours()) {
      return;
    }

    const film: FilmSaisie = {
      titre: this.titre().trim(),
      realisateur: this.realisateur().trim() || null,
      dateSortie: this.dateSortie() || null,
      genre: this.genre(),
    };
    const id = this.id();
    const requete = id === undefined ? this.service.creer(film) : this.service.modifier(Number(id), film);

    this.erreur.set('');
    this.envoiEnCours.set(true);
    requete.pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: enregistre => this.router.navigate(['/films', enregistre.id]),
      error: () => {
        this.erreur.set('Enregistrement impossible. Vérifie les champs et que le backend est démarré.');
        this.envoiEnCours.set(false);
      },
    });
  }
}
