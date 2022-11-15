# Workshops ŁDI, 2022

## Contact to me

mail: zielinskimaciej at yahoo.com

## Presentation

https://prezi.com/i/edit/xzhyrnr7hct9/

## How to build application

`./gradle clean build`

## How to start environment:

### Kafka cluster

`docker-compose -f docker/kafka-stack.yml up -d`

#### URL for Topics

http://localhost:3030/kafka-topics-ui/

### Postgres database (schema is created by application)

`docker-compose -f docker/postgres-stack.yml up -d`

#### Db Adminer:

* URL: http://localhost:8081/
* Connection Host (docker host on linux): 172.17.0.1
* User: postgres
* Password: secret

## How to build and start native-image application

### Build

`./gradlew dockerBuildNative`

### Start

`docker stack deploy -c docker/native-image.yml docker`

### Remove

`docker stack rm docker`

### Micronaut 3.7.3 Documentation

- [User Guide](https://docs.micronaut.io/3.7.3/guide/index.html)
- [API Reference](https://docs.micronaut.io/3.7.3/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.7.3/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)

---

- [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)

### Feature jdbc-hikari documentation

- [Micronaut Hikari JDBC Connection Pool documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jdbc)

### Feature kafka documentation

- [Micronaut Kafka Messaging documentation](https://micronaut-projects.github.io/micronaut-kafka/latest/guide/index.html)

### Feature test-resources documentation

- [Micronaut Test Resources documentation](https://micronaut-projects.github.io/micronaut-test-resources/latest/guide/)

### Feature security documentation

- [Micronaut Security documentation](https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html)

### Feature security-jwt documentation

- [Micronaut Security JWT documentation](https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html)

### Feature hibernate-jpa documentation

- [Micronaut Hibernate JPA documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#hibernate)

### Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)

## Feature openapi documentation

- [Micronaut OpenAPI Support documentation](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html)

- [https://www.openapis.org](https://www.openapis.org)

## Feature swagger-ui documentation

- [Micronaut Swagger UI documentation](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html)

- [https://swagger.io/tools/swagger-ui/](https://swagger.io/tools/swagger-ui/)
