[![SonarCloud Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=Knorrke_socialbotnet&metric=alert_status)](https://sonarcloud.io/dashboard?id=Knorrke_socialbotnet)
[![SonarCloud Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Knorrke_socialbotnet&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=Knorrke_socialbotnet)
[![SonarCloud Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Knorrke_socialbotnet&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=Knorrke_socialbotnet)

# SocialBotNet

[SocialBotNet.de](https://www.socialbotnet.de) ist ein didaktisches, soziales Netzwerk mit einer einfachen API speziell für Bots für den Einsatz im Unterricht. Der Code baut auf [eh3rrera/minitwit](https://github.com/eh3rrera/minitwit) auf und verwendet das [Javalin Framework](https://github.com/javalin/javalin).

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Material](#material)
- [Eigener Server](#eigener-server)
  - [Alternativ: Lokaler Server](#alternativ-lokaler-server)
- [API](#api)
  - [Daten abrufen](#daten-abrufen)
    - [`/api/users` - Alle registrierten Nutzer abrufen](#apiusers---alle-registrierten-nutzer-abrufen)
    - [`/api/posts` - Alle Posts abrufen](#apiposts---alle-posts-abrufen)
    - [`/api/pinnwand/:username` - Posts der Pinnwand eines Users](#apipinnwandusername---posts-der-pinnwand-eines-users)
  - [Daten senden](#daten-senden)
- [Server einrichten](#server-einrichten)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Material
Auf der Webseite gibt es [Projektvorlagen und Handouts](https://www.socialbotnet.de/material) sowie [Beispielprojekte und didaktische Informationen](https://www.socialbotnet.de/didaktik).

## Eigener Server
Mit einem Account bei [Heroku](https://heroku.com) lässt sich der Server mit nur einem Klick selbst bereitstellen:

[![Deploy auf Heroku](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/Knorrke/socialbotnet)  

**Einschränkungen**: Die kostenlose Datenbank ist beschränkt auf 10.000 Einträge (User, Posts und Likes) und die Anwendung wird nach 30 Minuten Inaktivität in einen Ruhezustand versetzt, sodass der erste Seitenaufruf eine gewisse Zeit dauert.

### Alternativ: Lokaler Server
Um den Server lokal laufen zu lassen, muss die .jar-Datei aus den [releases](https://github.com/Knorrke/socialbotnet/releases) heruntergeladen und über den folgenden Befehl gestartet werden.
```sh
$ java -jar pfad/zur/socialbotnet-3.0-jar-with-dependencies.jar
```
Dieser Befehlt startet einen jetty-Server auf [localhost:30003](http://localhost:30003), **jedoch ohne persistente Datenbank**. Diese müsste extra über die Umgebungsvariablen `JDBC_DATABASE_URL`, `JDBC_DATABASE_USERNAME` und `JDBC_DATABASE_PASSWORD` konfiguriert werden.


Es ist auch möglich, das SocialBotNet komplett auf einem eigenen remote Server mittels Dokku zu hosten. Details zur Einrichtung sind unten im Abschnitt [" Server einrichten"](#server-einrichten).


## API
Das Netzwerk bietet eine leicht zu bedienende API um User- und Postdaten abzurufen und Beiträge zu schreiben oder liken.

### Daten abrufen
Mit HTTP-GET können User- und Post-Daten abgerufen werden. Alle Anfragen können mit den GET-Parametern `sortby` nach jedem Attribut sortiert werden (default: `id`, mit `order` kann die Reihenfolge auf-/absteigend (`asc`/`desc`) sortiert werden und die Anzahl kann mit `limit` verändert werden (default ist 50)

Die folgenden HTTP-GET Schnittstellen werden unterstützt:

#### `/api/users` - Alle registrierten Nutzer abrufen  
**Parameter:** `sortby` (optional), `order` (optional) `limit` (optional)  
**Beispiel Ergebnis**: Anfrage an `/api/users`  
```json
[
  {
    "id":1,
    "username":"root",
    "hobbies":"doing stuff",
    "about":"I am root"
  },
  {
    "id":2,
    "username":"user002",
    "hobbies":"",
    "about":""
  }
]
```

#### `/api/posts` - Alle Posts abrufen
**Parameter:** `sortby` (optional), `order` (optional) `limit` (optional)  
**Beispiel Ergebnis**: Anfrage an`/api/posts?sortby=likes&limit=1`  
```json
[
  {
    "id": 3,
    "message": "This is a post written by user user001 to user004",
    "user": { "id": 1, "username": "user001", "hobbies": "", "about": "" },
    "wall": { "id": 4, "username": "user004", "hobbies": "", "about": "" },
    "publishingDate": "2014-07-14 09:46:28",
    "likedBy": [
      { "id": 1, "username": "user001", "hobbies": "", "about": "" },
      { "id": 5, "username": "user005", "hobbies": "", "about": "" },
      { "id": 6, "username": "user006", "hobbies": "", "about": "" },
      { "id": 7, "username": "user007", "hobbies": "", "about": "" }
    ]
  }
]
```

#### `/api/pinnwand/:username` - Posts der Pinnwand eines Users 
**Parameter:** `sortby` (optional), `order` (optional) `limit` (optional)  
**Beispiel Ergebnis**: Genau wie bei `/api/posts` nur gefiltert nach der korrekten `wall`.


### Daten senden
**Hinweis:** Um diese Schnittstelle nutzen zu können, benötigst du Logindaten des Users. Du musst daher bei allen Anfragen immer `username` und `password` mitsenden.

Die folgenden HTTP-Post Anfragen werden unterstützt:


- `/api/user/update`: Aktualisiere das Nutzerprofil  
**Parameter:** `username`, `password`, `newUsername` (optional), `hobbies` (optional), `about` (optional) 

- `/api/post`: Erstelle einen neuen Post an deiner Pinnwand.  
**Parameter:** `username`, `password`, `message`

- `/api/post/:username`: Erstelle einen neuen Post an der Pinnwand von `:username`  
**Parameter:** `username`, `password`, `message`

- `/api/like`: Like einen Post  
**Parameter:** `username`, `password`, `postid`

- `/api/unlike`: Ent-Like einen Post  
**Parameter:** `username`, `password`, `postid`

## Server einrichten
Um das SocialBotNet komplett selbst zu hosten, benötigst du nur einen Server mit ssh-Zugriff.

1. (SERVER) Installiere [dokku](https://github.com/dokku/dokku). Folge dabei der [Installationsanleitung](https://github.com/dokku/dokku#installation).  
**Hinweis**: Vergiss nicht, die web-basierte Installation durchzuführen!
2. (SERVER) Erstelle die Dokku app:  
```sh
$ dokku apps:create socialbotnet
```
3. (SERVER) Installiere das Plugin für PostgreSQL:   
```sh
$ sudo dokku plugin:install https://github.com/dokku/dokku-postgres.git postgre
```
4. (SERVER) Erstelle die Postgres Datenbank und Verbinde dich zur App  
```sh
$ dokku postgres:create socialbotnet-db
$ dokku postgres:link socialbotnet-db socialbotnet
```
5. (LOCAL) Klone das Repository und füge den Remote für Dokku hinzu:  
```
$ git clone https://github.com/Knorrke/socialbotnet.git
$ cd socialbotnet
$ git remote add dokku dokku@ip.or.domain:socialbotnet
````
6. (LOCAL) Deploye die Anwendung
```
$ git push dokku
```

