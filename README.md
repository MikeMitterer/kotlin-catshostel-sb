# CatsHostel - Spring Boot Version 
> [KTor-Version](https://github.com/MikeMitterer/kotlin-catshostel-kt)
> [MyBatis](https://blog.mybatis.org/)    
> [Spring Boot - Security](https://www.javainuse.com/spring/boot-jwt)  
> [Token-based API authentication with Spring and JWT](https://blog.softtek.com/en/token-based-api-authentication-with-spring-and-jwt)

## Features

   - iBatis (works with SQLite or Postgres-DB)
   - JWT support (mimics KeyCloak-Token)

## Database

    # cd to DB-Directory
    cd resources/db
    
    # Import schema (Postgres works just fine here)
    sqlite catshostel.db < sqlite-1-schema.sql

Set your working dir to $MODULE_WORKING_DIR$

![WorkingDir](doc/images/working-dir.png)