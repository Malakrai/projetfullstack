import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Film } from './film.model';

@Injectable({ providedIn: 'root' })
export class FilmService {
  private http = inject(HttpClient);

  getAll() {
    return this.http.get<Film[]>('/api/films');
  }

  supprimer(id: number) {
    return this.http.delete<void>(`/api/films/${id}`);
  }
}
