import { Acteur } from './acteur.model';

export type Genre = 'ACTION' | 'COMEDIE' | 'DRAME' | 'HORREUR' | 'SCIENCE_FICTION';

export interface Film {
  id: number;
  titre: string;
  realisateur: string | null;
  dateSortie: string | null;
  genre: Genre | null;
  acteurs?: Acteur[];
}
