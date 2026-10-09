import { Component, input, output } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Film } from '../film.model';

@Component({
  selector: 'app-film-card',
  imports: [DatePipe, RouterLink],
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
