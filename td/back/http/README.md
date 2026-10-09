# Tester le TD avec REST Client

1. Demarrer PostgreSQL avec la base `Film_db` et definir `DB_PASSWORD`.
2. Dans `td/back/Film`, lancer `.\gradlew.bat bootRun`.
3. Ouvrir `films.http` ou `acteurs.http` dans VS Code avec l'extension REST Client.
4. Cliquer sur **Send Request** pour chaque requete, dans l'ordre du fichier.

Les identifiants sont recuperes dans les reponses de creation. Pour rejouer un
scenario termine, recommencer a la premiere requete. `update` conserve les
tables et les donnees PostgreSQL entre deux demarrages.

## Lire le code

- Le controller recoit et renvoie les DTO : les donnees JSON de l'API.
- Le service contient les operations et appelle le repository.
- Le repository lit et enregistre les entites dans la base.
- Le mapper copie les champs entre une entite et un DTO.
- `Film.acteurs` gere la table `film_acteur`; `Acteur.films` est l'autre sens.
- `GET /films/{id}` renvoie un `FilmDetailDto` avec ses acteurs.
- Les acteurs sont des `ActeurDto` sans films, pour eviter les boucles JSON.
  Les routes `/films/{id}/acteurs` et `/acteurs/{id}/films` existent aussi.

Par defaut, ces deux routes utilisent les methodes `findBy...` des repositories.
Ajouter `?avecQuery=true` utilise la version avec `@Query` pour comparer.

`WebConfig` autorise les frontends locaux sur les ports 5173, 4200 et 3000.
Adapter `allowedOrigins` si le frontend utilise une autre adresse.

Les tests utilisent une base PostgreSQL separee, `films-tests`.
La creer une seule fois dans pgAdmin depuis le Query Tool de la base `postgres` :

```sql
CREATE DATABASE "films-tests";
```

Definir `DB_PASSWORD`, puis lancer `.\gradlew.bat test` dans `td/back/Film`.
Par defaut, les tests se connectent a `localhost:5432` avec l'utilisateur
`postgres`. `DB_HOST`, `DB_PORT` et `DB_USER` permettent de les modifier.
Les tables de `films-tests` sont recreees puis supprimees par les tests.
