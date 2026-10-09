import { Routes } from '@angular/router';
import { FilmList } from './film-list/film-list';
import { ActeurList } from './acteur-list/acteur-list';

export const routes: Routes = [
  { path: 'films', component: FilmList },
  { path: 'acteurs', component: ActeurList },
  { path: '', redirectTo: 'films', pathMatch: 'full' },
  { path: '**', redirectTo: 'films' },
];
