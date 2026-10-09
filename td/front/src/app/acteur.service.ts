import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Acteur } from './acteur.model';
import { Film } from './film.model';
import { Page } from './page.model';

export type TriActeur = 'nom' | 'prenom';
export type SensTri = 'asc' | 'desc';

@Injectable({ providedIn: 'root' })
export class ActeurService {
  private http = inject(HttpClient);
  private url = '/api/acteurs';

  getAll() {
    return this.http.get<Acteur[]>(this.url);
  }

  getPage(page: number, taille: number, tri: TriActeur, sens: SensTri) {
    const params = new HttpParams().set('page', page).set('taille', taille).set('tri', tri).set('sens', sens);
    return this.http.get<Page<Acteur>>(`${this.url}/page`, { params });
  }

  getById(id: number) {
    return this.http.get<Acteur>(`${this.url}/${id}`);
  }

  getFilms(id: number) {
    return this.http.get<Film[]>(`${this.url}/${id}/films`);
  }
}
