# CatsHostel - Spring Boot Version 
> [KTor-Version](https://github.com/MikeMitterer/kotlin-catshostel-kt)
> [MyBatis](https://blog.mybatis.org/)    
> [Spring Boot - Security](https://www.javainuse.com/spring/boot-jwt)  
> [Token-based API authentication with Spring and JWT](https://blog.softtek.com/en/token-based-api-authentication-with-spring-and-jwt)   
> [Spring Boot + KeyCloak](https://www.baeldung.com/spring-boot-keycloak)
> [KeyCloak - Spring Security Adapter](https://www.keycloak.org/docs/latest/securing_apps/#_spring_security_adapter)  

## Features

   - iBatis (works with SQLite or Postgres-DB)
   - JWT support (mimics KeyCloak-Token)
   - conditional Tests (e.g. ProtectedControllerTest)
   - configurable WebSecurity (use.keycloak=true|false)  
     use.keycloak=false uses WebSecurityConfig  
     use.keycloak=true uses KeycloakConfig  
    
## UI

   - [Chat](http://localhost:8080/ws.html)
   - [Freemarker-Template-Response](http://localhost:8080/) (StaticPages.kt)

## Swagger

   - [HTML](http://localhost:8080/swagger-ui.html)
   - [JSON](http://localhost:8080/api-docs)
   
### OpenAPI
   
   - [JSON](http://localhost:8080/v3/api-docs)
      
## Api

   - [Greeting](http://localhost:8080/greeting)       
   - [Greeting (Austrian style)](http://localhost:8080/servus)       
   - [Throws Exception](http://localhost:8080/exception)   
   
## WebSocket (Chat)

   - [Night chat](http://localhost:8080/ws.html)   
   
Can be configured in `application.properties`

```properties
# catshostel.websocket=chatserver
catshostel.websocket=simple
```       

You can send a "Ping" via Get-Request:

   - [Ping to WS](http://localhost:8080/ping)   
   
## Coroutines 

Check out my [BasicController](https://github.com/MikeMitterer/kotlin-catshostel-sb/blob/master/src/at/mikemitterer/catshostel/routes/BasicController.kt)

   - [Wait...](http://localhost:8080/wait?seconds=15)       

## Database

    # cd to DB-Directory
    cd resources/db
    
    # Import schema (Postgres works just fine here)
    sqlite catshostel.db < sqlite-1-schema.sql

Set your working dir to $MODULE_DIR$

![WorkingDir](doc/images/working-dir.png)

## Swagger

   - [UI](http://localhost:8080/swagger-ui.html)
   - [Api (JSON)](http://localhost:8080/v2/api-docs)
   
## KeyCloak Settings

![Realm](doc/images/realm.png)   
![Clients](doc/images/clients.png)   
![Roles](doc/images/roles.png)   
![Users](doc/images/users.png)   
![User - Cat1](doc/images/user-cat1.png)   
![User - Cat1 - Roles](doc/images/user-cat1-roles.png)   
![User - Nicki](doc/images/user-nicki.png)   
![User - Nicki - Roles](doc/images/user-nicki-roles.png)   
   
   
      