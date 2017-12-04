# Minimal Social Network

This is a minimal social network with bot friendly API for didactical purposes. The code is highly influenced by [eh3rrera/minitwit](https://github.com/eh3rrera/minitwit) using the Spark Framework.
This social network is intended for students to create a social bot for.

A working example can be visited on [https://social-bot-net.herokuapp.com/](https://social-bot-net.herokuapp.com/), check it out!

## Build instructions

Build the project using Maven and run it. Afterwards open [localhost:30003](http://localhost:30003) for the Minimal Social Network.

## API

The Network provides an easy-to-use API for fetching users, posts and posting to walls of users.

### Fetching data
The following GET requests are supported:

- GET request to `/api/users`: Receive all users registered on the minimalsocialnetwork  
**Example output**  
```
[{"id":2,"username":"user002","hobbies":"football","about":"I am user002"},{"id":1,"username":"user001","hobbies":"telekinesis","about":"I am user001"}]
```

- GET request to `/api/posts`: Receive all posts created on the minimalsocialnetwork  
**Example output**  
```
[
  {
  	"id":2,
  	"message":"Hello",
  	"user":{"id":1,"username":"user001"},
  	"wall":{"id":5,"username":"user005"},
  	"publishingDate":"Oct 25, 2014 10:09:55 AM",
  	"likedBy":[
  		{"id":1,"username":"user001"},
  		{"id":10,"username":"user010"}
	]
  }
]
```

- GET request to `/api/pinnwand/:username`: Receive all posts created on the minimalsocialnetwork posted to `:username`s wall  
**Example output**  
The output is exactly as from `/api/posts/`, but filtered after username of wall.

### Posting data
For these api calls it is necessary to provide user credentials! So include the params `username` and `password` in all requests.

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