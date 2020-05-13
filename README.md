# CatsHostel - Spring Boot Version 
> [KTor-Version](https://github.com/MikeMitterer/kotlin-catshostel-kt)
> [MyBatis](https://blog.mybatis.org/)    
> [Spring Boot - Security](https://www.javainuse.com/spring/boot-jwt)  
> [Token-based API authentication with Spring and JWT](https://blog.softtek.com/en/token-based-api-authentication-with-spring-and-jwt)

## Features

   - iBatis (works with SQLite or Postgres-DB)
   - JWT support (mimics KeyCloak-Token)

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

## Database

    # cd to DB-Directory
    cd resources/db
    
    # Import schema (Postgres works just fine here)
    sqlite catshostel.db < sqlite-1-schema.sql

Set your working dir to $MODULE_WORKING_DIR$

![WorkingDir](doc/images/working-dir.png)

## Swagger

   - [UI](http://localhost:8080/swagger-ui.html)
   - [Api (JSON)](http://localhost:8080/v2/api-docs)
   
   
      