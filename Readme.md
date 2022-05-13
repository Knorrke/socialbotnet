# SocialBotNet

[![SonarCloud Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=Knorrke_socialbotnet&metric=alert_status)](https://sonarcloud.io/dashboard?id=Knorrke_socialbotnet)
[![SonarCloud Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Knorrke_socialbotnet&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=Knorrke_socialbotnet)
[![SonarCloud Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Knorrke_socialbotnet&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=Knorrke_socialbotnet)
[![Deploy auf Heroku](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/Knorrke/socialbotnet)  

[English version below.](#english-version)  

[SocialBotNet.de](https://www.socialbotnet.de) ist ein didaktisches, soziales Netzwerk mit einer einfachen API speziell für Bots für den Einsatz im Unterricht. Der Code baut auf [eh3rrera/minitwit](https://github.com/eh3rrera/minitwit) auf und verwendet das SparkJava Frameworks.

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

  - [Material](#material)
  - [Eigener Server](#eigener-server)
  - [API](#api)
    - [Daten abrufen](#daten-abrufen)
    - [Daten senden](#daten-senden)
  - [Eigenen Server einrichten](#eigenen-server-einrichten)
    - [Alternative 1: Jar-Datei lokal starten](#alternative-1-jar-datei-lokal-starten)
      - [(Optional): Builden der .jar-Datei mit Maven](#optional-builden-der-jar-datei-mit-maven)
      - [Starten der jar Datei](#starten-der-jar-datei)
      - [Was ist mit der Datenbank, wenn man es lokal startet?](#was-ist-mit-der-datenbank-wenn-man-es-lokal-startet)
    - [Alternative 2: SocialBotNet auf Heroku deployen](#alternative-2-socialbotnet-auf-heroku-deployen)
    - [Alternative 3: SocialBotNet auf einem anderen Server mit Dokku installieren.](#alternative-3-socialbotnet-auf-einem-anderen-server-mit-dokku-installieren)
- [English Version](#english-version)
  - [Material](#material-1)
  - [Running own Server](#running-own-server)
  - [API](#api-1)
    - [Fetching data](#fetching-data)
    - [Posting data](#posting-data)
  - [Build instructions](#build-instructions)
    - [Alternative 1: Running the .jar file locally](#alternative-1-running-the-jar-file-locally)
      - [(Optional): Building .jar file with Maven](#optional-building-jar-file-with-maven)
      - [Running the .jar file](#running-the-jar-file)
      - [What about the Database when running locally?](#what-about-the-database-when-running-locally)
    - [Alternative 2: Deploying the SocialBotNet to Heroku](#alternative-2-deploying-the-socialbotnet-to-heroku)
    - [Alternative 3: Deploying the SocialBotNet to a different server](#alternative-3-deploying-the-socialbotnet-to-a-different-server)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Material
Auf der Webseite findest [Projektvorlagen und Handouts](https://www.socialbotnet.de/material) sowie [Beispielprojekte und didaktische Informationen](https://www.socialbotnet.de/didaktik).

## Eigener Server
Mit einem Account bei [Heroku](https://heroku.com) lässt sich der Server mit nur einem Klick selbst bereitstellen:

[![Deploy auf Heroku](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/Knorrke/socialbotnet)  

**Einschränkungen**: Die kostenlose Datenbank ist beschränkt auf 10.000 Einträge (User, Posts und Likes) und die Anwendung wird nach 30 Minuten Inaktivität in einen Ruhezustand versetzt, sodass der erste Seitenaufruf eine gewisse Zeit dauert.

* Um den Server lokal laufen zu lassen, muss die .jar-Datei aus den [releases](https://github.com/Knorrke/socialbotnet/releases) heruntergeladen und über den folgenden Befehl gestartet werden.
```sh
$ java -jar pfad/zur/socialbotnet-3.0-jar-with-dependencies.jar
```
Dieser Befehlt startet einen jetty-Server auf [localhost:30003](http://localhost:30003), **jedoch ohne persistente Datenbank**.

Es ist auch möglich, das SocialBotNet komplett auf einem eigenen Server mittels Dokku zu hosten. Details zur Einrichtung sind unten im Abschnitt ["Eigenen Server einrichten"](#eigenen-server-einrichten).


## API
Das Netzwerk bietet eine leicht zu bedienende API um User- und Postdaten abzurufen und Beiträge zu schreiben oder liken.

### Daten abrufen
Mit HTTP-GET können User- und Post-Daten abgerufen werden. Alle Anfragen können mit den GET-Parametern `sortby` nach jedem Attribut sortiert werden (default: `id`, mit `order` kann die Reihenfolge auf-/absteigend (`asc`/`desc`) sortiert werden und die Anzahl kann mit `limit` verändert werden (default ist 50)

Die folgenden HTTP-GET Schnittstellen werden unterstützt:

- `/api/users`: Liefert die registrierten Nutzer
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

- `/api/posts`: Liefert die Posts.
**Parameter:** `sortby` (optional),`order` (optional) `limit` (optional)
**Beispiel Ergebnis**: Anfrage an`/api/posts?sortby=likes&limit=1`
```json
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

- `/api/pinnwand/:username`: Erhalte die Posts, die auf `:username`s Pinnwand geschrieben wurden. 
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

## Eigenen Server einrichten
### Alternative 1: Jar-Datei lokal starten
Lade die .jar-Datei aus den [Releases](https://github.com/Knorrke/socialbotnet/releases) oder build sie selbst mit Maven.

#### (Optional): Builden der .jar-Datei mit Maven
Klone das Projekt in ein lokales Verzeichnis
```
git clone https://github.com/Knorrke/socialbotnet.git
cd socialbotnet
```

Wenn du eine IDE wie Eclipse verwendest, die Maven unterstützt, kannst du einfach `app.Main` starten. Ansonsten führe den folgenden Befehl aus, um eine .jar-Datei zu erzeugen.
```
mvn clean package
```

#### Starten der jar Datei
Wenn du die .jar-Datei hast, starte sie mit dem Befehl:

```sh
$ java -jar Pfad\zu\socialbotnet-3.0-jar-with-dependencies.jar
```
(Maven generiert die .jar-Datei in `target\socialbotnet-3.0-jar-with-dependencies.jar`

Öffne anschließend [localhost:30003](http://localhost:30003) für das SocialBotNet.

#### Was ist mit der Datenbank, wenn man es lokal startet?
Beim Starten wird eine temporäre Embedded PostgreSQLL-Datenbank erzeugt, die zur Laufzeit verwendet und danach wieder gelöscht wird.
Für eine persistente Datenbank muss lokal eine (leere) PostgreSQL Datenbank erstellt und über die Umgebungsvariablen `JDBC_DATABASE_URL`, `JDBC_DATABASE_USERNAME` und `JDBC_DATABASE_PASSWORD` konfiguriert werden.

### Alternative 2: SocialBotNet auf Heroku deployen
Mit nur einem Klick kannst du das SocialBotNet auf heroku starten:
[![Deploy auf Heroku](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/Knorrke/socialbotnet)

### Alternative 3: SocialBotNet auf einem anderen Server mit Dokku installieren.
Du benötigst ssh-Zugriff zu dem Server.

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

# English Version
This is a minimal social network with bot friendly API for educational purposes. The code is based on [eh3rrera/minitwit](https://github.com/eh3rrera/minitwit) using the SparkJava Framework.
This social network is intended for students to create a social bot for.

A running server can be visited on [https://www.socialbotnet.de](https://www.socialbotnet.de), check it out!

## Material
Materials only exist in german. You find them on the [materials subpage](https://www.socialbotnet.de/material) on the website to help you get started with programming your Bot in Java.

## Running own Server
With a free account [heroku](https://heroku.com) you can deploy the server with just one click:

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/Knorrke/socialbotnet)  

**Limitations**: The free plan for the database is limited to 10.000 entries (users, posts and likes) and the service will sleep after 30 minutes of inactivity. This will cause delays for the first visit after inactivity.

For a local server download the .jar file from [releases](https://github.com/Knorrke/socialbotnet/releases) and run it using the following command:
```sh
$ java -jar pfad/zur/socialbotnet-3.0-jar-with-dependencies.jar
```
This will start the Server at [localhost:30003](http://localhost:30003) **but without a persistent database**.

You can also run SocialBotNet on your own server using Dokku. For detailed instructions see [build instructions](#build-instructions) below.

## API

The Network provides an easy-to-use API for fetching users, posts and posting to walls of users.

### Fetching data
The API provides user and posts data via HTTP-GET requests. Every request supports the GET params `sortby` for any attribute (default `id`), `order` for `asc`/`desc` order (default `desc`) and `limit` (default `50`)

The following GET requests are supported:

- GET request to `/api/users`: Receive all users registered on the SocialBotNet    
**params:** optional: `sortby`, optional: `order`, optional: `limit`  
**Example output**: Request to `/api/users?limit=2`  
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

- GET request to `/api/posts`: Receive all posts created on the SocialBotNet.  
**params:** optional: `sortby`, optional: `order`, optional: `limit`  
**Example output**: Request to `/api/posts?sortby=likes&limit=1`
```json
{
  "id": 3,
  "message": "This is a post written by user user001 to user004",
  "publishingDate": "2014-07-14 09:46:28",
  "user": { "id": 1, "username": "user001", "hobbies": "", "about": "" },
  "wall": { "id": 4, "username": "user004", "hobbies": "", "about": "" },
  "likes": 4,
  "likedBy": [
    { "id": 1, "username": "user001", "hobbies": "", "about": "" },
    { "id": 5, "username": "user005", "hobbies": "", "about": "" },
    { "id": 6, "username": "user006", "hobbies": "", "about": "" },
    { "id": 7, "username": "user007", "hobbies": "", "about": "" }
  ]
}
```

- GET request to `/api/pinnwand/:username`: Receive all posts created on the SocialBotNet posted to `:username`s wall.  
**Example output**  
The output is exactly as from `/api/posts/`, but filtered after username of wall.

### Posting data
**Note:** For these API calls it is necessary to provide user credentials! So include the params `username` and `password` in all requests.

The following POST requests are supported:


- POST request to `/api/user/update`: Update the user profile 
**params:**  
`username`, `password`, optional: `newUsername`, optional: `hobbies`, optional: `about` 

- POST request to `/api/post`: Create a new post on your on wall.  
**params:**  
`username`, `password`, `message`

- POST request to `/api/post/:username`: Create a new post on `:username`s wall  
**params:**  
`username`, `password`, `message`

- POST request to `/api/like`: Like a post 
**params:**  
`username`, `password`, `postid`

- POST request to `/api/unlike`: Unlike a post 
**params:**  
`username`, `password`, `postid`

## Build instructions

### Alternative 1: Running the .jar file locally
Download the .jar file from [releases](https://github.com/Knorrke/socialbotnet/releases) or build it yourself.
#### (Optional): Building .jar file with Maven
Clone the project into a local repository
```
git clone https://github.com/Knorrke/socialbotnet.git
cd socialbotnet
```

If you are using an IDE supporting Maven (e.g. Eclipse), you should be able to just run the `app.Main` class. Otherwise build the project using Maven 
```
mvn clean package
```
#### Running the .jar file
Once you got the .jar file, run it:

```
java -jar path\to\socialbotnet-3.0-jar-with-dependencies.jar
```
(Maven will build the jar to `target\socialbotnet-3.0-jar-with-dependencies.jar`

Afterwards open [localhost:30003](http://localhost:30003) for the SocialBotNet.

#### What about the Database when running locally?
Running the SocialBotNet for the first time will create an empty HSQL database in `./database/database`. If you just want to test something, you can pass `--debug` to the jar execution. With this option the database is only created in memory and prefilled with some test data.

### Alternative 2: Deploying the SocialBotNet to Heroku
You can deploy the app to heroku with just one click: [![Deploy to Heroku](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/Knorrke/socialbotnet)  

### Alternative 3: Deploying the SocialBotNet to a different server
You need to have ssh access to that server.

1. (SERVER) Install [dokku](https://github.com/dokku/dokku) by following the [installation instructions](https://github.com/dokku/dokku#installation).  
**NOTE**: Do not forget to complete the web-based installation!
2. (SERVER) Create the Dokku app:  
```sh
$ dokku apps:create socialbotnet
```
3. (SERVER) Install dokku plugin for PostgreSQL:   
```sh
$ sudo dokku plugin:install https://github.com/dokku/dokku-postgres.git postgres
```
4. (SERVER) Create Postgres database and connect to app  
```sh
$ dokku postgres:create socialbotnet-db
$ dokku postgres:link socialbotnet-db socialbotnet
```
5. (LOCAL) Clone the repository and add a remote for dokku:  
```sh
$ git clone https://github.com/Knorrke/socialbotnet.git
$ cd socialbotnet
$ git remote add dokku dokku@ip.or.domain:socialbotnet
```
6. (LOCAL) Deploy the app
```sh
$ git push dokku
```
