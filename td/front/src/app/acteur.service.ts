import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Acteur } from './acteur.model';

@Injectable({ providedIn: 'root' })
export class ActeurService {
  private http = inject(HttpClient);

  getAll() {
    return this.http.get<Acteur[]>('/api/acteurs');
  }
}
