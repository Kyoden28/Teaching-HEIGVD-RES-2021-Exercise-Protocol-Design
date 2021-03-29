Author : Christian Gomes

# Phase 1

Ce protocole permet de spécifier comment la communication client-serveur sera effectué. 



### What transport protocol do we use?

Le protocole TCP/IP sera utilisé pour la communication réseau.

### How does the client find the server (addresses and ports)?

Le port par défaut sera le 4000. Le client connaîtra l'adresse du serveur auquel communiquer.

### Who speaks first?

Le client parle en premier en demandant une connexion au serveur. 

### What is the sequence of messages exchanged by the client and the server? (flow)

Le client peut envoyer les messages suivantes : 

- **CONNECT** : Demande une connexion au serveur
- **QUIT** : Déconnexion du serveur
- **CALCULATE [OPTIONS]** :  Demande au serveur de faire une opération
  - **OPTIONS** : ADD / MUL / SUB / DIV / MOD

Le serveur peut envoyer les messages suivantes : 

- **ERROR** : Indique une erreur au serveur 
- **RESPONSE** : Indique la réponse d'une opération au serveur

Le client envoie un message au serveur, le serveur a deux possiblités de réponse qui sont un message d'erreur ou une réponse. 

### What happens when a message is received from the other party? (semantics)

#### Communication  : 

**Connexion :** 

Client : CONNECT

Serveur : RESPONSE OK 

**Déconnexion :**

Client : QUIT

Serveur : RESPONSE OK 

**Opérations :** 

Client : CALCULATE ADD 2 5

Serveur : RESPONSE 7

-----

Client : CALCULATE DIV 10 5

Serveur : RESPONSE 2

**Erreur** :

Client : CONNECT 

Serveur  : ERROR - "Message"

------

Client : CALCULATE DIV 2 0

Serveur  : ERROR - "Message"

### What is the syntax of the messages? How we generate and parse them? (syntax)

Une connexion est demandé via : CONNECT 

Le serveur repond : RESPONSE OK 

Une opération est generé de la façon suivante : COMMAND [Opération] [Nombre1] [Nombre2]

Le serveur réalise l'opération et répond : RESPONSE [Résultat de l'opération]

### Who closes the connection and when?

Le client envoie une demande de déconnexion , à la réception du OK du serveur , il se déconnecte. Sans réponse, on peut imaginer un timeout pour ne pas être bloqué. 