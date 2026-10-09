import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { vi } from 'vitest';
import { FilmList } from './film-list';

describe('FilmList', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FilmList],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
  });

  afterEach(() => {
    TestBed.inject(HttpTestingController).verify();
    vi.restoreAllMocks();
  });

  function afficherFilm() {
    const http = TestBed.inject(HttpTestingController);
    const fixture = TestBed.createComponent(FilmList);
    fixture.detectChanges();
    http.expectOne('/api/films').flush([
      { id: 7, titre: 'Film de test', realisateur: 'Test', dateSortie: null, genre: null },
    ]);
    fixture.detectChanges();
    const bouton = fixture.nativeElement.querySelector('button') as HTMLButtonElement;
    return { http, fixture, bouton };
  }

  it('supprime via la carte et retire le film seulement après le succès HTTP', () => {
    vi.spyOn(window, 'confirm').mockReturnValue(true);
    const { http, fixture, bouton } = afficherFilm();
    bouton.click();
    fixture.detectChanges();
    const requete = http.expectOne('/api/films/7');
    expect(requete.request.method).toBe('DELETE');
    expect(bouton.disabled).toBe(true);
    expect(fixture.nativeElement.textContent).toContain('Film de test');
    requete.flush(null, { status: 204, statusText: 'No Content' });
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('app-film-card')).toBeNull();
    expect(fixture.nativeElement.textContent).toContain('Aucun film enregistré.');
  });

  it('ne contacte pas l’API si la suppression est annulée', () => {
    vi.spyOn(window, 'confirm').mockReturnValue(false);
    const { http, fixture, bouton } = afficherFilm();
    bouton.click();
    http.expectNone('/api/films/7');
    expect(fixture.nativeElement.textContent).toContain('Film de test');
  });

  it('conserve le film et affiche une erreur si la suppression échoue', () => {
    vi.spyOn(window, 'confirm').mockReturnValue(true);
    const { http, fixture, bouton } = afficherFilm();
    bouton.click();
    http.expectOne('/api/films/7').flush({}, { status: 500, statusText: 'Server Error' });
    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('Film de test');
    expect(fixture.nativeElement.textContent).toContain('Suppression impossible.');
    expect(bouton.disabled).toBe(false);
  });
});
