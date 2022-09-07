## Task statement: Show app logs in Kibana dashboard.

### Tips:

- Add dependency to build.gradle:  implementation 'net.logstash.logback:logstash-logback-encoder:7.2'

- Create logback-spring.xml (I use prod profile) with LogstashTcpSocketAppender

- Configure logstash.conf

- Configure docker-compose.yml with ELK and run it

- Check that all services runs fine and open Kibana UI [http://localhost:5601/]()

- Run backend city-service in docker-compose.yml with "SPRING_PROFILES_ACTIVE=prod"

- GET /micro-city-library-be or GET _cat/indices

- Create index pattern micro-city-library-be

- Go to [http://localhost:5601/app/discover]() page to see logs

### Notes:

**We need link logstash service to network with backend service for communications.**

In my case

`docker network ls`  =>

348ce96d337c elk_elk c853e8a607a4 city-library-be_web

`docker ps`    =>  202f21e13405 logstash:7.16.2

We can use this command:

`docker network connect --alias logstash city-library-be_web 202f21e13405`

**alternative**, use provided name of existed network (web) and parameter **external**: true in docker-compose
[https://www.baeldung.com/ops/docker-network-information]()

