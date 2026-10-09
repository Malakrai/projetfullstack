import { Acteur } from './acteur.model';

export const GENRES = ['ACTION', 'COMEDIE', 'DRAME', 'HORREUR', 'SCIENCE_FICTION'] as const;

export type Genre = (typeof GENRES)[number];

export interface RoleFilm {
  acteurId: number;
  personnage: string;
}

export interface Film {
  id: number;
  titre: string;
  realisateur: string | null;
  dateSortie: string | null;
  genre: Genre | null;
  acteurs?: Acteur[];
  roles?: RoleFilm[];
}
