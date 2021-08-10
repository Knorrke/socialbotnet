# SocialBotNet

[english version below](#english-version)
[SocialBotNet.de](https://www.socialbotnet.de) ist ein didaktisches, soziales Netzwerk mit einer einfachen API speziell für Bots für den Einsatz im Unterricht. Der Code orientiert sich an [eh3rrera/minitwit](https://github.com/eh3rrera/minitwit) und verwendet Verwendung das SparkJava Frameworks.

Du kannst auch deinen eigenen Server starten, indem du
* die jar-Datei aus den [releases](https://github.com/Knorrke/socialbotnet/releases) startest. Diese startet einen jetty-Server auf [localhost:30003](http://localhost:30003).
* Mit nur einem Klick: [![Deploy auf Heroku](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/Knorrke/socialbotnet)
* Richte es auf deinem eigenen Webserver ein.

**Details dazu findest du unten im Abschnitt ["Eigenen Server einrichten"](#eigenen-server-einrichten).**

## Material
Auf der Webseite findest [Projektvorlagen und Handouts](https://www.socialbotnet.de/material) sowie [Beispielprojekte und didaktische Informationen](https://www.socialbotnet.de/didaktik).

## API
Das Netzwerk bietet eine leicht zu bedienende API um User- und Postdaten abzurufen und Beiträge zu schreiben oder liken.

### Daten abrufen
Die folgenden HTTP-GET Anfragen werden unterstützt:

- `/api/users`: Liefert die letzten 100 registrierten Nutzer.  
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

- `/api/posts`: Liefert die Posts. Sie können mit dem GET-Parameter `sortby` nach `likes`, `time` (default) oder `trending` sortiert werden. Die Anzahl der Posts kann mit dem Parameter `limit` verändert werden (default ist 50).  
**Parameter:** `sortby` (optional), `limit` (optional)
**Beispiel Ergebnis**: Anfrage an`/api/posts?sortby=likes&limit=1`
```
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

- `/api/pinnwand/:username`: Erhalte die Posts, die auf `:username`s Pinnwand geschrieben wurden. Auch sie können mit dem GET-Parameter `sortby` nach `likes`, `time` (default) oder `trending` sortiert werden und die Anzahl kann mit dem Parameter `limit` verändert werden (default ist 50).
**Parameter:** `sortby` (optional), `limit` (optional)
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
### Builden der .jar-Datei mit Maven (optional)
**Hinweis: Du kannst diesen Schritt überspringen und die Datei aus den [Releases](https://github.com/Knorrke/socialbotnet/releases) herunterladen.** 
Klone das Projekt in ein lokales Verzeichnis
```
git clone https://github.com/Knorrke/socialbotnet.git
cd socialbotnet
```

Wenn du eine IDE wie Eclipse verwendest, die Maven unterstützt, kannst du einfach `app.Main` starten. Ansonsten führe den folgenden Befehl aus, um eine .jar-Datei zu erzeugen.
```
mvn clean package
```

### .Jar-Datei lokal starten .jar file locally
Nachdem du die .jar-Datei heruntergeladen oder mit Maven gebuildet hast, starte sie mit dem Befehl:

```
java -jar Pfad\zu\socialbotnet-1.1-jar-with-dependencies.jar
```
(Maven generiert die .jar-Datei in `target\socialbotnet-1.1-jar-with-dependencies.jar`

Öffne anschließend [localhost:30003](http://localhost:30003) für das SocialBotNet.

#### Database
Beim ersten Starten wird eine leere HSQL-Datenbank in `./database/database` erzeugt. Falls du nur etwas testen möchtest, kannst du `--debug` bei der Ausführung anhängen. Dadurch wird nur eine Datenbank zur Laufzeit mit initialen, vorgefertigten Testdaten verwendet.

### SocialBotNet auf Heroku deployen
Mit nur einem Klick kannst du das SocialBotNet auf heroku starten:
[![Deploy auf Heroku](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/Knorrke/socialbotnet)

### SocialBotNet auf einem anderen Server mit Dokku installieren.
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

## English Version
This is a minimal social network with bot friendly API for educational purposes. The code is highly influenced by [eh3rrera/minitwit](https://github.com/eh3rrera/minitwit) using the SparkJava Framework.
This social network is intended for students to create a social bot for.

A running server can be visited on [https://www.socialbotnet.de](https://www.socialbotnet.de), check it out!

You can also run your own server by either
* running the jar file from the [releases](https://github.com/Knorrke/socialbotnet/releases) or building it using Maven. It will start a jetty server on [localhost:30003](http://localhost:30003).
* deploying it to a webserver

**For detailed instructions see [build instructions](#build-instructions) below**

## Material
You find some [materials](https://www.socialbotnet.de/material) on the website to help you get started with programming your Bot in Java.

## API

The Network provides an easy-to-use API for fetching users, posts and posting to walls of users.

### Fetching data
The following GET requests are supported:

- GET request to `/api/users`: Receive all users registered on the SocialBotNet  
**Example output**: Request to `/api/users`  
```
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

- GET request to `/api/posts`: Receive all posts created on the SocialBotNet. They can be sorted by `likes` or `time` (default) using the `sortby` handle. Also the number of results can be limited by `limit` (default 50).  
**params:** optional: `sortby`, optional: `limit`  
**Example output**: Request to `/api/posts?sortby=likes&limit=1`
```
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

- GET request to `/api/pinnwand/:username`: Receive all posts created on the SocialBotNet posted to `:username`s wall. They can be sorted by `likes` or `time` (default) using the `sortby` handle. Also the number of results can be limited by `limit` (default 50)  
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

### Building .jar file with Maven (optional)
Clone the project into a local repository
```
git clone https://github.com/Knorrke/socialbotnet.git
cd socialbotnet
```

If you are using an IDE supporting Maven (e.g. Eclipse), you should be able to just run the `app.Main` class. Otherwise build the project using Maven 
```
mvn clean package
```

### Running the .jar file locally
After downloading the .jar file from the [releases](https://github.com/Knorrke/socialbotnet/releases) or building it using Maven run it:

```
java -jar path\to\socialbotnet-1.1-jar-with-dependencies.jar
```
(Maven will build to jar to `target\socialbotnet-1.1-jar-with-dependencies.jar`

Afterwards open [localhost:30003](http://localhost:30003) for the SocialBotNet.

#### Database
Running the SocialBotNet for the first time will create an empty HSQL database in `./database/database`. If you just want to test something, you can pass `--debug` to the jar execution. With this option the database is only created in memory and prefilled with some test data.

### Deploying the SocialBotNet to Heroku
1. Sign in to [Heroku](https://www.heroku.com/) and create a new App. 
2. On the dashboard of your app go to resources and search for the Postgres addon and add it to your app.
3. Clone the SocialBotNet by running
```
git clone https://github.com/Knorrke/socialbotnet.git
```
4. Follow the instructions of heroku for deployment (see Deploy tab on your dashboard). This should be something along those lines:
```sh
cd socialbotnet
heroku login

heroku git:remote -a my-app-name
git push heroku
```
5. Wait for the app to be deployed and then check on `my-app-name.heroku.com`

### Deploying the SocialBotNet to a different server
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
