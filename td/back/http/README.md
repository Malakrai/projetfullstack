# Tester le TD avec REST Client

1. Demarrer PostgreSQL avec la base `Film_db` et definir `DB_PASSWORD`.
2. Dans `td/back/Film`, lancer `.\gradlew.bat bootRun`.
3. Ouvrir `films.http` ou `acteurs.http` dans VS Code avec l'extension REST Client.
4. Cliquer sur **Send Request** pour chaque requete, dans l'ordre du fichier.

Les identifiants sont recuperes dans les reponses de creation. Pour rejouer un
scenario termine, recommencer a la premiere requete. `create-drop` recree les
tables a chaque demarrage et les supprime a l'arret de l'application.

## Lire le code

- Le controller recoit et renvoie les DTO : les donnees JSON de l'API.
- Le service contient les operations et appelle le repository.
- Le repository lit et enregistre les entites dans la base.
- Le mapper copie les champs entre une entite et un DTO.
- `Film.acteurs` gere la table `film_acteur`; `Acteur.films` est l'autre sens.
- Les DTO ne contiennent pas les relations. On les consulte avec les routes
  `/films/{id}/acteurs` et `/acteurs/{id}/films`, sans boucle JSON.

Par defaut, ces deux routes utilisent les methodes `findBy...` des repositories.
Ajouter `?avecQuery=true` utilise la version avec `@Query` pour comparer.

`WebConfig` autorise les frontends locaux sur les ports 5173, 4200 et 3000.
Adapter `allowedOrigins` si le frontend utilise une autre adresse.

Les tests se lancent avec `.\gradlew.bat test` dans `td/back/Film`.
Ils utilisent H2, une base temporaire en memoire : PostgreSQL reste la base de
l'application. Les tests H2 ne remplacent pas une verification sur PostgreSQL.
