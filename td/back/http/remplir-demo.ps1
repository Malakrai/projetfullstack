$ErrorActionPreference = 'Stop'
$baseUrl = 'http://localhost:8081'

# Lancer le backend avant ce script. Les donnees existantes sont conservees.
$films = @(
    @{ titre = 'Interstellar'; realisateur = 'Christopher Nolan'; dateSortie = '2014-11-05'; genre = 'SCIENCE_FICTION' },
    @{ titre = 'Inception'; realisateur = 'Christopher Nolan'; dateSortie = '2010-07-21'; genre = 'SCIENCE_FICTION' },
    @{ titre = 'Blade Runner'; realisateur = 'Ridley Scott'; dateSortie = '1982-06-25'; genre = 'SCIENCE_FICTION' },
    @{ titre = 'Titanic'; realisateur = 'James Cameron'; dateSortie = '1997-12-19'; genre = 'DRAME' }
)
$acteurs = @(
    @{ nom = 'McConaughey'; prenom = 'Matthew' },
    @{ nom = 'Hathaway'; prenom = 'Anne' },
    @{ nom = 'DiCaprio'; prenom = 'Leonardo' },
    @{ nom = 'Ford'; prenom = 'Harrison' },
    @{ nom = 'Winslet'; prenom = 'Kate' }
)
$filmsExistants = Invoke-RestMethod "$baseUrl/films"
$acteursExistants = Invoke-RestMethod "$baseUrl/acteurs"
$filmIds = @{}
$acteurIds = @{}

foreach ($film in $films) {
    $existant = $filmsExistants | Where-Object { $_.titre -eq $film.titre -and $_.realisateur -eq $film.realisateur } | Select-Object -First 1
    if (-not $existant) {
        $existant = Invoke-RestMethod "$baseUrl/films" -Method Post -ContentType 'application/json' -Body ($film | ConvertTo-Json)
    }
    $filmIds[$film.titre] = $existant.id
}
foreach ($acteur in $acteurs) {
    $existant = $acteursExistants | Where-Object { $_.nom -eq $acteur.nom -and $_.prenom -eq $acteur.prenom } | Select-Object -First 1
    if (-not $existant) {
        $existant = Invoke-RestMethod "$baseUrl/acteurs" -Method Post -ContentType 'application/json' -Body ($acteur | ConvertTo-Json)
    }
    $acteurIds[$acteur.nom] = $existant.id
}
$associations = @(
    @{ film = 'Interstellar'; acteur = 'McConaughey' },
    @{ film = 'Interstellar'; acteur = 'Hathaway' },
    @{ film = 'Inception'; acteur = 'DiCaprio' },
    @{ film = 'Blade Runner'; acteur = 'Ford' },
    @{ film = 'Titanic'; acteur = 'DiCaprio' },
    @{ film = 'Titanic'; acteur = 'Winslet' }
)
foreach ($lien in $associations) {
    $filmId = $filmIds[$lien.film]
    $acteurId = $acteurIds[$lien.acteur]
    Invoke-RestMethod "$baseUrl/films/$filmId/acteurs/$acteurId" -Method Put | Out-Null
}
$resultat = Invoke-RestMethod "$baseUrl/films"
$resultat | Select-Object id, titre, realisateur | Format-Table
