import { Routes } from '@angular/router';
import { FilmList } from './film-list/film-list';
import { FilmForm } from './film-form/film-form';
import { FilmDetail } from './film-detail/film-detail';
import { ActeurList } from './acteur-list/acteur-list';
import { ActeurDetail } from './acteur-detail/acteur-detail';
import { NotFound } from './not-found/not-found';

export const routes: Routes = [
  { path: 'films', component: FilmList },
  { path: 'films/nouveau', component: FilmForm },
  { path: 'films/:id/modifier', component: FilmForm },
  { path: 'films/:id', component: FilmDetail },
  { path: 'acteurs', component: ActeurList },
  { path: 'acteurs/:id', component: ActeurDetail },
  { path: '', redirectTo: 'films', pathMatch: 'full' },
  { path: '**', component: NotFound },
];
