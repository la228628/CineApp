Sprint 1: 20-03-2024 au 03-04-2024

Objectif: Affichage et modification des films
________________________________________________________

Suivi de Planification du Projet:

1. Implémentation des fonctionnalités:
    - Fonctionnalité 1: Affichage des film du coté client [Terminé]
    - Fonctionnalité 2: Modification des films du coté client [Terminé]
    - Fonctionnalité 3: Suppression des films du coté client [Terminé]
    - Fonctionnalité 4: Création de la base de données [Terminé]
    - Fonctionnalité 5: Création de la table Movie [Terminé]

2.Motivation des choix de conception:
    - Nous avons choisi de créer une DAO pour gérer les accès à la base de données.
________________________________________________________

| Tâche                                                               | Développeurs Assignés | Statut (En cours/Terminé) |
|---------------------------------------------------------------------|-----------------------|---------------------------|
| Design de l'interface utilisateur                                   | Lucas / Raphaël       | Terminé                   |
| Création de la table Movie dans la db                               | Lucas                 | Terminé                   |
| Interface gérant et modification des films                          | Louis / Dylan         | Terminé                   |
| Suppression des films                                               | Louis / Dylan         | Terminé                   |
| Affichage des films du coté client                                  | Raphaël / Lucas       | Terminé                   |
| Création de la base de données                                      | Lucas                 | Terminé                   |
| Création de la classe Movie avec ses attributs et ses <br/>méthodes | Dylan                 | Terminé                   |
| Communication entre les différentes fenêtres de l'application       | Lucas                 | Terminé                   |
| Creation de la javadoc                                              | Tous                  | Terminé                   |
| Tests unitaires                                                     | Louis /Dylan          | Terminé                   |
| Implémentation de la DAO                                            | Raphaël               | Terminé                   |

----------------------------------------------------------------------------------------------------------------------------
| Fonctionnalité                            | Statut   | Estimation des points |
|-------------------------------------------|----------|-----------------------|
| Affichage des films du côté client        | Terminé  | 5                     |
| Modification des films du côté client     | Terminé  | 8                     |
| Suppression des films du côté client      | Terminé  | 5                     |
| Création de la base de données            | Terminé  | 6                     |
| Création de la table Movie                | Terminé  | 3                     |
| **Total des points:**                     |          | **34**                |


...

Sprint 2: 04-04-2024 au 11-04-2024

Objectif: Gestion des séances et des tickets
________________________________________________________

Suivi de Planification du Projet:

1. Implémentation des fonctionnalités:
    - Fonctionnalité 1: Affichage des séances du coté client [Terminé]
    - Fonctionnalité 2: Modification des séances du coté client [Terminé]
    - Fonctionnalité 3: Suppression des séances du coté client [Terminé]
    - Fonctionnalité 4: Possibilité de choisir des images depuis n'importe quel répertoire [Terminé]
    - Fonctionnalité 5: Création de la table Ticket [Terminé]
    - Fonctionnalité 6: Permettre à l'utilisateur de chosisir son ticket [En cours]
    - Fonctionnalité 7: Permettre à l'utisateur d'accéder à son compte [Terminé]

2.Motivation des choix de conception:
    - Nous avons choisi de créer des méthodes statiques pour gérer les fichiers images.
________________________________________________________

| Tâche                                                              | Développeurs Assignés | Statut (En cours/Terminé) |
|--------------------------------------------------------------------|-----------------------|---------------------------|
| Interface utilisateur pour la gestion des séances                  | Dylan                 | Terminé                   |
| Création de la table Ticket dans la db                             | Lucas                 | Terminé                   |
| Possibilité de choisir des images depuis n'importe quel répertoire | Dylan                 | Terminé                   |
| Creation de la javadoc                                             | Tous                  | Terminé                   |
| Tests unitaires                                                    | Raphaël, Louis        | Terminé                   |
| Implémentation de la DAO                                           | Raphaël,Dylan,Louis   | Terminé                   |
| Interface pour que l'utilisateur puisse accéder à son compte       | Louis                 | Terminé                   |
----------------------------------------------------------------------------------------------------------------------------

| Fonctionnalité                                                     | Statut  | Estimation des points |
|--------------------------------------------------------------------|---------|-----------------------|
| Interface utilisateur pour la gestion des séances                  | Terminé | 5                     |
| Création de la table Ticket dans la db                             | Terminé | 3                     |
| Possibilité de choisir des images depuis n'importe quel répertoire | Terminé | 5                     |
| Interface pour que l'utilisateur puisse accéder à son compte       | Terminé | 5                     |
| **Total des points:**                                              |         | ** 18**               |

...

Sprint 3: 11-04-2024 au 17-04-2024

Objectif: Refactoring, gestion des conflits, inscription et tickets
________________________________________________________

Suivi de Planification du Projet:

1. Implémentation des fonctionnalités:
   - Fonctionnalité 1: Hashage du mots de passe des clients [Terminé]
   - Fonctionnalité 2: Possibilité qu'un client s'inscrive [Terminé]
   - Fonctionnalité 3: Permettre a l'utilisateur d'acheter et d'afficher ses tickets dans son interface client [Terminé]
   - Fonctionnalité 4: Ajout d'héritage et de polymorphisme [Terminé]
   - Fonctionnalité 5: Refactoring de la gestion d'erreurs (une partie) [Terminé]
   - Fonctionnalité 6: Affichage des détails des films (synopsis) [Terminé]
   - Fonctionnalité 7: Ajout de contraintes cohérentes pour la création de scéances [Terminé]

2.Motivation des choix de conception:
    - Nous avons choisi de créer des classes abstraites pour gérer les différents types de "films".

| Tâche                                                              | Développeurs Assignés | Statut (En cours/Terminé) |
|--------------------------------------------------------------------|-----------------|---------------------------|
| Hashage du mots de passe des clients                               | Louis           | Terminé                   |
| Possibilité qu'un client s'inscrive                                | Raphael         | Terminé                   |
| Permettre a l'utilisateur d'acheter et d'afficher ses tickets dans son interface client | Raphael-Lucas   | Terminé                   |
| Ajout d'héritage et de polymorphisme                               | Louis-Dylan     | Terminé                   |
| Refactoring de la gestion d'erreurs                                 | Lucas           | Terminé                   |
| Affichage des détails des films (synopsis)                         | Lucas           | Terminé                   |
| Ajout de contraintes cohérentes pour la création de scéances       | Dylan           | Terminé                   |
| Creation de la javadoc                                             | Tous            | Terminé                   |
| Tests unitaires                                                    | Tous            | Terminé                   |


| Fonctionnalité                                                     | Statut  | Estimation des points |
|--------------------------------------------------------------------|---------|-----------------------|
| Hashage du mots de passe des clients                               | Terminé | 3                     |
| Possibilité qu'un client s'inscrive                                | Terminé | 5                     |
| Permettre a l'utilisateur d'acheter et d'afficher ses tickets dans son interface client | Terminé | 5                     |
| Ajout d'héritage et de polymorphisme                               | Terminé | 4                     |
| Refactoring de la gestion d'erreurs                                 | Terminé | 3                     |
| Affichage des détails des films (synopsis)                         | Terminé | 2                     |
| Ajout de contraintes cohérentes pour la création de scéances       | Terminé | 3                     |
| **Total des points:**                                              |         | **25**                |
