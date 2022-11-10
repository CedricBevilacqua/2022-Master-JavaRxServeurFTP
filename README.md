# ServeurFTP-BEVILACQUA-ALTINKAYNAK

Création d'un logiciel ayant le comportement d'un serveur FTP <br>
Altinkaynak Sema, Bevilacqua Cedric<br>
15/02/22

## Ce qui a été implémenté

La très grande majorité des attendus du cahier des charges ont été implémentées.
On retrouve notamment : <br>
    - Connexion au serveur<br>
    - Rejet sur identifiant ou mot de passe incorrectes<br>
    - Possibilité de lister le contenu du répertoire et de rentrer dedans puis revenir en arrière<br>
    - Impossibilité de sortir du dossier du serveur par des retour arrières<br>
    - Possibilité de télécharger, mettre en ligne, supprimer, renommer, déplacer un fichier ainsi que de créer des répertoires<br>
    - La connexion peut se couper sans faire crasher le serveur de manière à ce qu'il puisse accepter de nouvelles connexions<br>
    - Support de multi connexions à l'aide de threads<br>
    - Possibilité de configurer en paramètres le port, ainsi que le dossier du serveur et les identifiants<br>
<br>
Le mode actif n'a cependant pas été implémenté, seul le mode passif est pris en charge au travers de la nouvelle commande EPSV.<br>
<br>
Le code a également été testé au travers de JUnit et commenté de manière à pouvoir générer une JavaDoc.
Un diagramme UML a également été réalisé dans le dossier "doc" qui se trouve à la racine du dépôt. Il permet de visualiser l'architecture qui a été utilisée.

## Conception

Notre projet contient deux classes principales : <br>
    - FTPServer : S'occupe de toute la partie gestion d'une connexion FTP avec un client en communiquant les réponses aux commandes et les données avec celui-ci.<br>
    - DataManager : S'occupe de toutes les interactions avec le système de fichiers du serveur. Il va concrétiser les actions d'enregistrement et de modification des fichiers sur le serveur FTP en allant modifier les fichiers.<br>

La classe FTPServer contient une instance de DataManager. Elle contient également deux autres classes DataConnector et CommandeConnector qui permettent de gérer les socket et tous les autres objets (Stream, Printer...) associés. Ces deux classes ont donc des méthodes simples d'écriture ou de lecture afin d'être facilement utilisées par la classe FTPServer qui ne va pas gérer directement la création et la gestion des socket.

On retrouve ensuite d'autres classes comme la classe Main qui récupére les arguments afin d'ajuster les paramètres du serveur et démarre toutes les connexions. La classe ConnectManager est utilisée pour attendre toute nouvelle connexion d'un client sur le port FTP.

## Fonctionnement

Dès le démarrage du programme, la classe main va lire les arguments afin de récupérer tous les paramètres du serveur. Ensuite, elle va initialiser une instance de ConnectManager et entrer dans une boucle qui va sans cesse attendre de nouvelles connexions.<br>

Le thread principal ne sert alors que à accepter de nouvelles connexions et à leur associer une nouvelle instance de FTPServer qui sera systématiquement exécutée dans un autre thread au travers d'un caller.<br>

Une classe FTPServer initialise une instance de DataManager afin de pouvoir modifier les fichiers du serveur en fonction des commandes qu'elle recevra ainsi qu'un canal de connexion avec le client au travers d'un CommandConnector.<br>

Ensuite, une boucle va s'exécuter sur la lecture d'une nouvelle commande envoyée par le client qui est bloquant. A chaque nouvelle commande, on appelle une méthode qui va analyser cette commande, lancer des actions sur le DataManager au besoin puis répondre au travers du canal de commandes ou encore envoyer ou recevoir des informations sur le canal de données si nécessaire.<br>

L'opération sera répetée jusqu'à ce qu'une exception se produise. Elle peut arriver lorsque le client tente d'effectuer une action interdite, impossible où qu'il ferme la connexion où que celle-ci est coupée. Lorsque cela se produit, l'exception sera catchée et une tentative de déconnexion des connecteurs démarre. L'exécution du thread est ensuite terminé.<br>

## Compiler et utiliser

Nous avons utilisé un projet de type Maven pour toute la conception de ce projet.

Pour pouvoir generer la javadoc, il vous suffira de vous placer dans le répertoire du projet et de taper la commande ***mvn javadoc:javadoc***, cela créera un dossier target qui contiendra un dossier docs. Le dossier docs contient les sources de la javadoc.<br>

Pour pourvoir tester le serveur, il vous suffit de suivre les commandes ci-dessous: <br>
    - Ouvrez un terminal de commande et placer vous dans le répertoire du projet, normalement vous verrez un dossier src et un fichier pom.xml, puis ecriver la commande suivante ***mvn package***. Vous venez de gènerer le jar et les fichiers executables dans le répertoire target
    - Il vous faudra toujours rester dans le répertoire du projet et puis ecrire la commande qui suit dans un terminal de commande : ***java -jar target/projet-ServeurFTP.jar***. Il vous faudra ajouter des élèments dans la ligne de commande afin de tester le projet. Le plus simple serait d'écrire ***java -jar target/projet-ServeurFTP.jar*** et d'ajouter le chemin d'un dossier. Nous verrons quelques explications au sujet des paramètres à ajouter. 


Notre serveur peu fonctionner avec différents arguments dans l'ordre suivant : <br>
    - Adresse du serveur (l'adresse du chemin contenant le répertoire devant être affichée par le serveur)<br>
    - Port (par défaut 21)<br>
    - Nom d'utilisateur (par défaut anonymous)<br>
    - Mot de passe (par défaut anonymous@example.com)<br>

Ces arguments peuvent être omis mais doivent être toujours dans le même ordre.
L'adresse du serveur est cependant obligatoire.

Ainsi, on peut lancer le serveur de ces deux manières : <br>
    - java -jar target/projet-ServeurFTP.jar "user/server"<br>
    - java -jar target/projet-ServeurFTP.jar "user/server" 21 anonymous anonymous@example.com<br>
