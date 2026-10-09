import { Component, input, output } from '@angular/core';
import { Film } from '../film.model';

@Component({
  selector: 'app-film-card',
  templateUrl: './film-card.html',
  styleUrl: './film-card.css',
})
export class FilmCard {
  film = input.required<Film>();
  desactive = input(false);
  supprimer = output<Film>();

  onSupprimer() {
    this.supprimer.emit(this.film());
  }
}
