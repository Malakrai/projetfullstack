import { Component, computed, input, output } from '@angular/core';
import { DatePipe, NgClass } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Film } from '../film.model';

@Component({
  selector: 'app-film-card',
  imports: [DatePipe, NgClass, RouterLink],
  templateUrl: './film-card.html',
  styleUrl: './film-card.css',
})
export class FilmCard {
  film = input.required<Film>();
  desactive = input(false);
  supprimer = output<Film>();

  estAncien = computed(() => {
    const date = this.film().dateSortie;
    return date !== null && new Date(date).getFullYear() < 2000;
  });

  onSupprimer() {
    this.supprimer.emit(this.film());
  }
}
