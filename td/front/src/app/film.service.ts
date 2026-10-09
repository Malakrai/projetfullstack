import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Film } from './film.model';

export type FilmSaisie = Omit<Film, 'id' | 'acteurs' | 'roles'>;

@Injectable({ providedIn: 'root' })
export class FilmService {
  private http = inject(HttpClient);
  private url = '/api/films';

  getAll() {
    return this.http.get<Film[]>(this.url);
  }

  getById(id: number) {
    return this.http.get<Film>(`${this.url}/${id}`);
  }

  creer(film: FilmSaisie) {
    return this.http.post<Film>(this.url, film);
  }

  modifier(id: number, film: FilmSaisie) {
    return this.http.put<Film>(`${this.url}/${id}`, film);
  }

  supprimer(id: number) {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  associerActeur(filmId: number, acteurId: number, personnage = '') {
    return this.http.post<void>(`${this.url}/${filmId}/acteurs/${acteurId}`, { personnage });
  }

  modifierRole(filmId: number, acteurId: number, personnage: string) {
    return this.http.put<void>(`${this.url}/${filmId}/acteurs/${acteurId}/role`, { personnage });
  }

  dissocierActeur(filmId: number, acteurId: number) {
    return this.http.delete<void>(`${this.url}/${filmId}/acteurs/${acteurId}`);
  }
}
