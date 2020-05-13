# TODO RESTful API

[![pipeline status](https://gitlab.com/gutmox/money-transfers/badges/master/pipeline.svg)](https://gitlab.com/gutmox/money-transfers/commits/master)

[![coverage report](https://gitlab.com/gutmox/money-transfers/badges/master/coverage.svg)](https://gitlab.com/gutmox/money-transfers/commits/master)

Persistent RESTful API for a “TODO” application. 

## Swagger API Definition

* https://app.swaggerhub.com/apis/gutmox/ToDo_Rest_API/1.0.0 

![Swagger definition](https://gitlab.com/gutmox/todos/raw/master/docs/swagger.png)  
 

## Project structure Layout

```
.
├── gradle                              - gradle wrapper
│   └── wrapper
├── docs                                - Swagger API doc
├── src/main/java                       - Source code
├── src/test/java                       - Unit tests
└── src/integration-test/java           - Integration tests 

```

## Package organisation

|package   	    | description|
|---	        |---	            |
|com.gutmox.todos.api    | Java API definition|
|com.gutmox.todos.api.impl    | Java API implementation|
|com.gutmox.todos.api.repositories    | repositories api|
|com.gutmox.todos.api.repositories.mongo    | repositories implementation on MongoDb|
|com.gutmox.todos.api.validations    | Validations classes to be used in composition from services implementacion|
|com.gutmox.todos.auth.*    | under this package structure are the login, logout, sign up and authentication classes, following a similar structure than the todo resources|
|com.gutmox.todos.config    | the configuration utils are centralised under this package|
|com.gutmox.todos.guice    | One class I had to add to glue guice with vert.x|
|com.gutmox.todos.handlers     | Vert.x Handlers to deal with request, organised by rest method|
|com.gutmox.todos.handlers.factory | centralised all the todo handlers creation|
|com.gutmox.todos.routing | Vert.x router configuration whith all endpoints and methods|
|com.gutmox.todos.verticles | Vert.x verticles|
|com.gutmox.todos.vertx.tools | a helper class to make easy run vert.x|

## Built With

* [Java 8](http://www.oracle.com/technetwork/java/javase/10-relnote-issues-4108729.html) - Code language 
* [Vert.x Web + Vert.x](https://vertx.io) - Reactive library
* [Google guice](https://github.com/google/guice) - Dependency injection library
* [Gradle](https://gradle.org) - Build tool

### Unit Testing

* [JUnit 4](https://junit.org/junit4/) - Junit
* [Mockito](http://site.mockito.org)   - Mocking 
* [AssertJ](http://joel-costigliola.github.io/assertj/index.html) - adds fluent assertion support


## Example /todos POST Sequence

[![](https://mermaid.ink/img/eyJjb2RlIjoic2VxdWVuY2VEaWFncmFtXG4gICAgcGFydGljaXBhbnQgQXBwXG4gICAgcGFydGljaXBhbnQgTG9naW5cbiAgICBwYXJ0aWNpcGFudCBUb2RvXG4gICAgQXBwLT4-IExvZ2luOiBQT1NUIC9hdXRoL2xvZ2luIHt1c2VybmFtZTosIHBhc3N3b3JkfVxuICAgIExvZ2luLS0-IEFwcDogcmV0dXJucyB7and0IHRva2VufVxuICAgIEFwcC0-PiBUb2RvOiBQT1NUIC90b2RvcyB7XCJ0aXRsZVwiOiBcInRpdGxlXCIsIFwiZGVzY3JpcHRpb25cIlwiZGVzY3JpcHRpb25cIiwgXCJwcmlvcml0eVwiOiAxfVxuICAgIE5vdGUgcmlnaHQgb2YgTG9naW46IFBhc3NpbmcgYXMgaGVhZGVyIFxcbiBBdXRob3JpemF0aW9uID0gand0IHRva2VuXG4gICAgVG9kby0tPiBBcHA6IGNyZWF0ZWQgaXRlbSIsIm1lcm1haWQiOnsidGhlbWUiOiJkZWZhdWx0In0sInVwZGF0ZUVkaXRvciI6ZmFsc2V9)

```mermaid
sequenceDiagram
    participant App
    participant Login
    participant Todo
    App->> Login: POST /auth/login {username:, password}
    Login--> App: returns {jwt token}
    App->> Todo: POST /todos {"title": "title", "description""description", "priority": 1}
    Note right of Login: Passing as header \n Authorization = jwt token
    Todo--> App: created item
```

## Running It manually

```
./mongo-start.sh

```

then, from the IDE, run the main class:

```
    com.gutmox.todos.Runner
```

## Running tests

### Manual tests

Follow next steps:

* Create an user through the signup endpoint
* Login with the previous obtained user, getting a JWT token to invoke the TODO API, which is passed as header. 
* Create a todo item, getting that items' uuid   
* Get a todo item   

#### User Sign up

```
curl -X POST \
  http://localhost:8080/auth/signup \
  -H 'Content-Type: application/json' \
  -H 'cache-control: no-cache' \
  -d '{
  "userName": "userName",
  "password": "password"
}'
``` 

#### User login to get a JWT Token
```
curl -X POST \
  http://localhost:8080/auth/login \
  -H 'Content-Type: application/json' \
  -H 'cache-control: no-cache' \
  -d '{
  "userName": "userName",
  "password": "password"
}'
```

#### Creating a todo item 

```
curl -X POST \
  http://localhost:8080/todos \
  -H 'Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InVzZXJOYW1lIiwiaWF0IjoxNTQ1MzgwMjc4LCJleHAiOjE1NDU1NTMwNzgsImF1ZCI6IlVzZXIiLCJpc3MiOiJIZXkgQ29ycC4ifQ.fjc3Q11u-pqwulzYaxYVj9Hc7O-eCOp28-UoGgkJ9y4' \
  -H 'Content-Type: application/json' \
  -d '{
  "title": "title",
  "description": "description",
  "priority": 1
}'
```
#### Getting a todo item 

```
curl -X GET \
  http://localhost:8080/todos/5c1ca62c05c14f062d1f0780 \
  -H 'Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InVzZXJOYW1lIiwiaWF0IjoxNTQ1MzgwMjc4LCJleHAiOjE1NDU1NTMwNzgsImF1ZCI6IlVzZXIiLCJpc3MiOiJIZXkgQ29ycC4ifQ.fjc3Q11u-pqwulzYaxYVj9Hc7O-eCOp28-UoGgkJ9y4' \
  -H 'Content-Type: application/json' \
  -H 'cache-control: no-cache' \
  -d '{
  "title": "title5",
  "description": "description5",
  "priority": 5
}'
```

### Unit tests
    
```
    gradle clean test
```

### Integration tests

```
    gradle clean execMongo test stopMongo -Dtest.profile=integration
```

## Building
```
    gradle clean build
```

## Docker

### To build:

```
    docker build -t todos . 
 ```
### To run:

```
    docker run -t -i -p 8080:8080 todos
```

## Sonar

### Report

[sonarcloud report](https://sonarcloud.io/dashboard?id=money-transfers)

### Running it locally

``` 
 gradle sonarqube \
  -Dsonar.organization=gutmox-github \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.login=xxxx

```
