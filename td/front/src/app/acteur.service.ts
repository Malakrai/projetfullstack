import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Acteur } from './acteur.model';
import { Film } from './film.model';

@Injectable({ providedIn: 'root' })
export class ActeurService {
  private http = inject(HttpClient);
  private url = '/api/acteurs';

  getAll() {
    return this.http.get<Acteur[]>(this.url);
  }

  getById(id: number) {
    return this.http.get<Acteur>(`${this.url}/${id}`);
  }

  getFilms(id: number) {
    return this.http.get<Film[]>(`${this.url}/${id}/films`);
  }
}
