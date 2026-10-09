# TP — Du repository au endpoint

## PostgreSQL et démarrage

Dans pgAdmin, se connecter à PostgreSQL et créer la base `covid-db`.
Le script `sql/creer-base.sql` peut être exécuté dans le Query Tool de la base
`postgres` si `covid-db` n'existe pas encore.

Depuis `tp/back`, fournir le mot de passe dans le terminal puis démarrer :

```powershell
$secret = Read-Host 'Mot de passe PostgreSQL' -AsSecureString
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new('', $secret).Password
.\gradlew.bat bootRun
```

Par défaut, la connexion utilise `localhost:5432`, l'utilisateur `postgres`
et la base `covid-db`. Les variables `DB_HOST`, `DB_PORT` et `DB_USER`
permettent de modifier ces paramètres. Ne pas écrire le mot de passe dans Git.

Hibernate utilise `ddl-auto: update` pour conserver les données à l'arrêt.
`data.sql` ajoute Malak, Dupont et leur médecin Martin s'ils sont absents,
puis remplit leur relation dans `docteur_patients`.

## Premier démarrage : `/hello`

`HelloController.hello()` retourne un objet `Message`. Cette classe contient
un attribut `texte`, un constructeur et un getter `getTexte()` que Jackson
utilise pour produire le JSON.

Dans `http/requete.http`, lancer les deux premières requêtes avec REST Client :

- Avec `Accept: application/json`, le résultat attendu est HTTP 200 et
  `{"texte":"Bonjour Malak"}`.
- Avec `Accept: application/xml`, le résultat attendu est HTTP 406
  (`Not Acceptable`) : aucun convertisseur XML n'est configuré pour cet objet.
  L'en-tête `Accept` indique le format de réponse souhaité par le client.

Au démarrage, vérifier dans les traces la création du contexte web et le
démarrage de Tomcat sur le port 8080.

## Implémentation

- `PatientRepository` étend `JpaRepository<Patient, Long>`.
- `PatientService` reçoit le repository par injection constructeur. L'ancien
  stockage `PatientStore` et ses implémentations ont été supprimés, ainsi que
  leur configuration manuelle.
- `GET /patients` appelle `findAll()` via le service.
- `GET /patients?nom=Malak` utilise la méthode dérivée `findByNom` (nom exact).
- `GET /patients/1` utilise `findById`.
- `GET /patients/1?avecMedecin=true` utilise la requête JPQL annotée `@Query`
  avec `LEFT JOIN FETCH p.medecinTraitant`. La jointure gauche conserve aussi
  un patient sans médecin. Un identifiant absent renvoie HTTP 404.

Les requêtes prêtes à lancer sont dans `http/requete.http`.

## Relation médecin–patients

Pour l'étape 8 « Entités et relations », `Docteur.patients` utilise
`@OneToMany` sans `mappedBy` et une `@JoinTable` nommée `docteur_patients`.
Cette association est unidirectionnelle. `Patient.medecinTraitant` conserve
son `@ManyToOne` : c'est maintenant une association indépendante.
Modifier une association ne met donc pas automatiquement l'autre à jour.

Comparaison du schéma attendu :

| Configuration | Stockage de la relation |
| --- | --- |
| Avant : `mappedBy = "medecinTraitant"` | Clé étrangère `patient.medecin_traitant_id`, partagée par les deux côtés |
| Après : `@OneToMany` avec `@JoinTable` | Table supplémentaire `docteur_patients` avec `docteur_id` et `patient_id` ; `patient.medecin_traitant_id` reste indépendant |

Les relations vers `Adresse` conservent les colonnes `adresse_id` dans
`patient` et `docteur`. La table de jointure possède deux clés étrangères et
une contrainte unique sur `patient_id` pour le `OneToMany`.

Après démarrage, observer les instructions Hibernate dans le terminal.
Dans pgAdmin, rafraîchir `covid-db > Schemas > public > Tables` puis exécuter
`sql/verifier-schema.sql` pour afficher les colonnes et contraintes réelles.
Les quatre tables attendues sont `adresse`, `patient`, `docteur` et
`docteur_patients`. Les traces du 30 septembre 2026 confirment leur création
sur PostgreSQL, ainsi que les clés étrangères et la contrainte unique.

## Frontière DTO

Avant l'ajout des DTO, la lecture JPA réussissait mais la conversion en JSON
échoue : Jackson tente de lire `Docteur.patients`, une collection lazy, alors
que la session Hibernate est fermée (`open-in-view: false`). Les logs indiquent
`HttpMessageNotWritableException` et `Cannot lazily initialize collection ... (no session)`.
Le niveau DEBUG de Spring Web est activé pour examiner le traitement HTTP.

Le service retourne maintenant des `PatientDto` avec uniquement `id`, `nom`
et `age`. `PatientMapper.toDto` copie ces trois champs sans lire les relations.
Le contrôleur ne manipule plus l'entité `Patient`. Le JSON attendu est plat :

```json
[{"id": 1, "nom": "Malak", "age": 22}]
```

Les valeurs dépendent des données de la base. Les relations de `Patient`
sont en `LAZY` pour éviter leur chargement inutile : `GET /patients` doit
exécuter un seul SELECT et renvoyer HTTP 200 sans erreur de sérialisation.
Le paramètre `avecMedecin=true` reste disponible pour l'exemple JOIN FETCH,
mais le médecin ne fait pas partie du DTO renvoyé.

`POST /patients` reçoit un `PatientCreationDto` sans identifiant :

```json
{"nom": "Alice", "age": 25}
```

Le service crée et sauvegarde le patient. La réponse HTTP 201 contient son
`PatientDto`, avec l'identifiant généré par la base.

## Observer les requêtes SQL

Exécuter les requêtes de `http/requete.http` et compter les SELECT dans les
traces PostgreSQL pour comparer le chargement classique et le JOIN FETCH.

Compter chaque instruction SQL une seule fois : si `show-sql` et le logger
`org.hibernate.SQL` sont activés simultanément, la même instruction apparaît deux
fois dans les traces, sans être exécutée deux fois.
