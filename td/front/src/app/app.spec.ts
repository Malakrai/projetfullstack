import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { App } from './app';

describe('App', () => {
  let http: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('affiche les films et acteurs reçus de l’API', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    http.expectOne('/api/films').flush([
      { id: 1, titre: 'Interstellar', realisateur: 'Christopher Nolan', dateSortie: '2014-11-05', genre: 'SCIENCE_FICTION' },
    ]);
    http.expectOne('/api/acteurs').flush([
      { id: 1, nom: 'Hathaway', prenom: 'Anne' },
    ]);
    fixture.detectChanges();
    const page = fixture.nativeElement as HTMLElement;
    expect(page.querySelector('h1')?.textContent).toContain('Bibliothèque de films');
    expect(page.textContent).toContain('Interstellar');
    expect(page.textContent).toContain('Christopher Nolan');
    expect(page.textContent).toContain('Anne Hathaway');
  });

  it('affiche un message pour les listes vides', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    http.expectOne('/api/films').flush([]);
    http.expectOne('/api/acteurs').flush([]);
    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('Aucun film enregistré.');
    expect(fixture.nativeElement.textContent).toContain('Aucun acteur enregistré.');
  });

  it('affiche une erreur lorsque l’API est indisponible', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    http.expectOne('/api/films').flush({}, { status: 503, statusText: 'Service Unavailable' });
    http.expectOne('/api/acteurs').flush({}, { status: 503, statusText: 'Service Unavailable' });
    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('Impossible de charger les films.');
    expect(fixture.nativeElement.textContent).toContain('Impossible de charger les acteurs.');
  });
});
