# Projet fullstack – Bibliothèque de films

Projet réalisé pendant les TD de développement fullstack par RAI Malak 
email: malak.rai8@etu.univ-lorraine.fr

L'idée : une petite application pour gérer des films et des acteurs. On peut voir la liste des films, en ajouter, les modifier, les supprimer, et associer des acteurs à un film.

Le dossier `td` contient deux parties (toutes les commandes ci-dessous se lancent depuis ce dossier `td`) :

- `back/Film` : l'API REST en Spring Boot (Java 21) avec une base PostgreSQL
- `front` : le front en Angular 21 qui utilise cette API

## Ce qu'il faut avoir installé

- Java 21
- PostgreSQL
- Node.js (j'ai utilisé la version 24) avec npm

Pas besoin d'installer Gradle, le projet a déjà le wrapper (`gradlew`).

## Étape 1 : créer la base de données

Il faut que PostgreSQL soit lancé (sur le port par défaut, 5432).

Ensuite, créer une base qui s'appelle `Film_db`, par exemple dans pgAdmin:

```sql
CREATE DATABASE "Film_db";
```

Pas besoin de créer les tables à la main : Spring les crée tout seul au démarrage (`ddl-auto=update`).

Pour les tests du back, il y a une autre base (`films-tests`), le script pour la créer est dans `back/Film/sql/creer-base-test.sql`.

## Étape 2 : lancer le back

Le back se connecte à PostgreSQL avec l'utilisateur `postgres`. Je n'ai pas mis le mot de passe dans le code, il faut le donner dans la variable d'environnement `DB_PASSWORD` avant de lancer.

Sur Windows (PowerShell) :

```powershell
cd back/Film
$env:DB_PASSWORD = "ton_mot_de_passe"
.\gradlew bootRun
```

Sur Mac / Linux :

```bash
cd back/Film
DB_PASSWORD=ton_mot_de_passe ./gradlew bootRun
```

Le back tourne sur le port **8081**. Pour vérifier que ça marche, ouvrir http://localhost:8081/films dans le navigateur : ça doit afficher du JSON.

## Étape 3 : lancer le front

Dans un autre terminal (le back doit rester lancé) :

```bash
cd front
npm install
npm start
```

`npm install` n'est utile que la première fois. Ensuite, aller sur http://localhost:4200.

## Comment le front et le back communiquent

Dans le front, je n'appelle jamais directement `http://localhost:8081`. Tous les appels commencent par `/api` (par exemple `/api/films`).

C'est le proxy d'Angular qui fait le lien. Il est configuré dans `front/src/proxy.conf.json` (et déclaré dans `angular.json`) :

```
le navigateur demande  http://localhost:4200/api/films
le proxy enlève /api   →  http://localhost:8081/films
```

Donc :

- il faut **lancer le back avant le front**, sinon l'application affiche un message d'erreur
- si on change le port du back, il faut changer `target` dans `proxy.conf.json` et relancer `npm start`

## Ce que fait l'application

**Films**
- liste des films, avec une recherche par titre ou par réalisateur
- les films sortis avant 2000 sont mis en évidence
- page de détail d'un film avec ses acteurs
- ajout et modification d'un film (le même formulaire sert pour les deux)
- suppression d'un film

**Acteurs**
- liste des acteurs avec une pagination et un tri par nom ou prénom (on clique sur le titre de la colonne)
- page de détail d'un acteur avec les films dans lesquels il a joué

**Liens film et acteur**
- dans la page d'un film, on peut ajouter un acteur avec une liste déroulante (les acteurs déjà dans le film n'y apparaissent pas)
- on peut aussi retirer un acteur du film

**Rôles**
- quand on ajoute un acteur à un film, on peut aussi écrire le personnage qu'il joue (ce n'est pas obligatoire)
- le rôle s'affiche à côté de l'acteur dans la page du film, et on peut l'ajouter ou le modifier après coup
- côté back, j'ai ajouté une entité `Role` (table `film_role`) qui garde le personnage pour chaque couple film / acteur

Si l'API est éteinte ou renvoie une erreur, un message s'affiche à la place.

## Les routes de l'API utilisées

| Méthode | URL (côté back) | À quoi ça sert |
|---|---|---|
| GET | `/films` | liste des films |
| GET | `/films/{id}` | un film avec ses acteurs et leurs rôles |
| POST | `/films` | ajouter un film |
| PUT | `/films/{id}` | modifier un film |
| DELETE | `/films/{id}` | supprimer un film |
| POST | `/films/{id}/acteurs/{acteurId}` | ajouter un acteur à un film (on peut envoyer `{ "personnage": "..." }`, c'est facultatif) |
| PUT | `/films/{id}/acteurs/{acteurId}/role` | modifier le personnage joué (si on envoie un personnage vide, le rôle est effacé) |
| DELETE | `/films/{id}/acteurs/{acteurId}` | retirer un acteur d'un film |
| GET | `/acteurs` | liste de tous les acteurs |
| GET | `/acteurs/page?page=0&taille=5&tri=nom&sens=asc` | acteurs par page, triés (`tri` peut être `nom`, `prenom` ou `id`) |
| GET | `/acteurs/{id}` | un acteur |
| GET | `/acteurs/{id}/films` | les films d'un acteur |

## Organisation du front

```
front/src/app/
├── film.model.ts, acteur.model.ts, page.model.ts   les interfaces (copiées sur les DTO du back)
├── film.service.ts, acteur.service.ts              tous les appels HTTP sont ici
├── film-list/       la liste des films + la recherche
├── film-card/       la carte d'un film (reçoit le film en input, envoie "supprimer" en output)
├── film-detail/     le détail d'un film (c'est lui qui appelle l'API)
├── film-acteurs/    la partie "acteurs et rôles" du détail d'un film (inputs + outputs, n'appelle pas l'API)
├── film-form/       le formulaire d'ajout / de modification
├── acteur-list/     la liste des acteurs (pagination + tri)
├── acteur-detail/   le détail d'un acteur et ses films
└── not-found/       la page affichée si l'URL n'existe pas
```

## Ce que j'ai utilisé en plus du cours

J'ai surtout utilisé ce qu'on a vu en cours, mais j'ai eu besoin de quelques trucs en plus :

### Côté front

- **`toObservable` + `switchMap`** : pour relancer la requête quand l'`id` de l'URL, la page ou le tri change (`toSignal` tout seul ne charge qu'une fois).
- **`forkJoin`** : pour charger un acteur et ses films en même temps.
- **`takeUntilDestroyed`** : pour que les `subscribe()` (ajout, modification, suppression) se ferment tout seuls quand on quitte la page.
- **`HttpParams`** : pour mettre les paramètres de pagination dans l'URL.
- **La locale française** (`LOCALE_ID` dans `app.config.ts`) : pour que les dates s'affichent en français.
- **`window.confirm`** : pour demander confirmation avant de supprimer un film.
- **`Omit<Film, ...>`** : comme `Partial`, mais pour enlever des champs (le formulaire n'envoie pas l'`id`).
- **`as const`** : pour écrire la liste des genres une seule fois.
- **`ngOnInit`** : pour charger le film à modifier au démarrage du formulaire.

### Côté back

- **`PageRequest` et `Sort`** : pour la pagination et le tri des acteurs (le cours en parle dans « pour aller plus loin »).
- **L'entité `Role`** : une table `film_role` pour stocker le personnage joué. J'ai gardé le `@ManyToMany` existant pour ne rien casser.
- **`deleteBy...`** : comme les `findBy...` du cours, mais pour supprimer les rôles quand on supprime un film ou un acteur.
- **`@RequestBody(required = false)`** : le personnage est facultatif quand on associe un acteur.
- **`ResponseStatusException`** : pour renvoyer une 404 si on modifie le rôle d'un acteur qui n'est pas dans le film.

## Les tags

- `TD1` et `td2` : la partie back (API Spring, DTO, base de données)
- `td3` : le front Angular branché sur l'API
