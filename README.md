# Customer File Server

## Build & Run

The project runs with H2 in memory database. On every startup all data will be deleted.

Project's default port is `8080`

### Build with `docker-compose`

```bash
docker-compose build
docker-compose up
```

### Build with `maven`

Project java version 11 or higher

```bash
mvn clean verify
java -jar target/files-0.0.1-SNAPSHOT.jar
```

## Documentation
When project runs, swagger document will be ready at http://localhost:8080/swagger-ui/index.html 

Also [swagger.json](swagger.json) can be used offline.