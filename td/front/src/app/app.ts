import { Component } from '@angular/core';
import { FilmList } from './film-list/film-list';
import { ActeurList } from './acteur-list/acteur-list';

@Component({
  selector: 'app-root',
  imports: [FilmList, ActeurList],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {}
