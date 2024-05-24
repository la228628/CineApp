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

Sprint 4: 18-04-2024 au 1-05-2024

Objectif: Gestion des exceptions, début du serveur
________________________________________________________

Suivi de Planification du Projet:

1. Implémentation des fonctionnalités:
   - Fonctionnalité 1: Début du serveur [Terminé]
   - Fonctionnalité 2: Gérer les exceptions du coté du serveur [Terminé]
   - Fonctionnalité 3: Création, modification, suppression d'un Viewable de type saga [Terminé]
   
2.Motivation des choix de conception:
- Implémentation de la logique de Socket pour la communication client-server.

| Tâche                                                                                   | Développeurs Assignés | Statut (En cours/Terminé) |
|-----------------------------------------------------------------------------------------|-----------------------|---------------------------|
| Début du server                                                                         | Raphael-Louis         | Terminé                   |
| Possibilité qu'un client affiche une saga                                               | Lucas                 | Terminé                   |
| Permettre a l'utilisateur d'acheter et d'afficher ses tickets dans son interface client | Raphael-Lucas         | Terminé                   |
| Refactoring de la gestion d'erreurs                                                     | Lucas                 | Terminé                   |
| Ajout de contraintes cohérentes pour la création de saga                                | Dylan                 | Terminé                   |
| Creation de la javadoc                                                                  | Tous                  | Terminé                   |
| Tests unitaires                                                                         | Tous                  | Terminé                   |


| Fonctionnalité                                                     | Statut  | Estimation des points |
|--------------------------------------------------------------------|---------|-----------------------|
| Début du serveur                                                   | Terminé | 5                     |
| Gérer les exceptions du coté du serveur                           | Terminé | 5                     |
| Création, modification, suppression d'un Viewable de type saga     | Terminé | 5                     |
| **Total des points:**                                              |         | **15**                |

Sprint 5: 2-05-2024 au 8-05-2024

Objectif: Gestion des exceptions, refactoring des Sockets et threads
________________________________________________________

Suivi de Planification du Projet:

1. Implémentation des fonctionnalités:
   - Fonctionnalité 1: Gérer les exceptions du coté du serveur [Terminé]
   - Fonctionnalité 2: Refactoring des Sockets et threads [Terminé]
   - Fonctionnalité 4: Gestion des javaFX et ServerRequestHandler thread [Terminé]

2.Motivation des choix de conception:
- Implémentation de la logique de Socket pour la communication client-server et des threads pour la gestion des requêtes.
- Utilisation de la classe ServerRequestHandler pour gérer les requêtes que le client envoie au serveur et la reception des réponses.
- Utilisation de la classe ClientHandler pour gérer les requêtes du client du coté du serveur.


| Tâche                                                                                   | Développeurs Assignés | Statut (En cours/Terminé) |
|-----------------------------------------------------------------------------------------|-----------------------|---------------------------|
| Gérer les exceptions du coté du serveur                                                | Lucas                 | Terminé                   |
| Refactoring des Sockets et threads                                                      | Louis                 | Terminé                   |
| Gestion des javaFX et ServerRequestHandler thread                                       | Dylan-Raphael         | Terminé                   |
| Creation de la javadoc                                                                  | Tous                  | Terminé                   |
| Tests unitaires                                                                         | Tous                  | Terminé                   |

| Fonctionnalité                                                     | Statut  | Estimation des points |
|--------------------------------------------------------------------|---------|-----------------------|
| Gérer les exceptions du coté du serveur                           | Terminé | 5                     |
| Refactoring des Sockets et threads                                 | Terminé | 5                     |
| Gestion des javaFX et ServerRequestHandler thread                  | Terminé | 5                     |
| **Total des points:**                                              |         | **15**                |

Sprint 6: 9-05-2024 au 24-05-2024

Objectif: Gestion des exceptions, refactoring des controllers du coté de l'admin, broadcast, double dispatche pour les requetes

Suivi de Planification du Projet:

1. Implémentation des fonctionnalités:
   - Fonctionnalité 1: Gérer les exceptions du coté du serveur [Terminé]
   - Fonctionnalité 2: Refactoring des controllers du coté de l'admin [Terminé]
   - Fonctionnalité 3: Broadcast [Terminé]
   - Fonctionnalité 4: Double dispatche pour les requetes [Terminé]

2.Motivation des choix de conception:
- Double-dispatch pour les requetes du client au serveur et la gestion des réponses a ces requetes.

| Tâche                                                                                   | Développeurs Assignés | Statut (En cours/Terminé) |
|-----------------------------------------------------------------------------------------|-----------------------|---------------------------|
| Gérer les exceptions du coté du serveur                                                | Lucas                 | Terminé                   |
| Refactoring des controllers du coté de l'admin                                           | Dylan-Raphael         | Terminé                   |
| Broadcast                                                                               | tous                  | Terminé                   |
| Double dispatche pour les requetes                                                      | Raphael-Louis         | Terminé                   |
| Creation de la javadoc                                                                  | Tous                  | Terminé                   |
| Tests unitaires                                                                         | Tous                  | Terminé                   |

| Fonctionnalité                                                     | Statut  | Estimation des points |
|--------------------------------------------------------------------|---------|-----------------------|
| Gérer les exceptions du coté du serveur                           | Terminé | 5                     |
| Refactoring des controllers du coté de l'admin                     | Terminé | 5                     |
| Broadcast                                                         | Terminé | 5                     |
| Double dispatche pour les requetes                                | Terminé | 5                     |
| **Total des points:**                                              |         | **20**                |

Explication de l'Appli-cine:

L'application Appli-cine est une application de gestion de cinéma.



Pour démarrer l'application, il faut lancer le serveur qui se trouve dans le package be/helha/applicine/server/Server.java.
Ce dernier va initier une connexion sur un port et sur une ip que l'on peux spécifier dans le fichier server.properties (PORT et HOST) avant de démarrer le serveur.

Ensuite, l'application client peut se connecter au serveur. Plusieurs instances de client peuvent se connecter au meme serveur en meme temps. Pour démarrer le client, il faut lancer le program qui se trouve dans be/helha/applicine/clientLauncher.java.

Lors du démarrage du client, une fenetre principale va apparaitre. Cette fenetre contient la liste des "films/saga" actuellement "disponible" dans notre cinéma. Une description détaillées de chaque films peux etre affichée en cliquant sur le bouton "plus d'info". Ainsi qu'un bouton acheter des tickets. Pour acheter des tickets il faut se connecter ou se créer un compte. Il faut aussi qu'une séance pour ce "film/saga" soit programmée par l'admin. Quand un client a acheté un film, il peux afficher les tickets qu'il possède en cliquant sur le bouton: "Mon compte". Là, il peux supprimer son compte ou gérer ses tickets.

Pour accéder a l'interface "admin", il faudra se connecter en tant qu'administrateur avec les identifiants suivants: login: admin, password: admin. Un administrateur peux modifier, supprimer, ajouter un film dans le premier onglets. Dans l'onglet suivant, l'admin peux créer une scéance qui contiendra un film ou plusieurs film si c'est une sage. Dans le troisième onglet, il peux créer des sagas contenants plusieurs film.
Rea