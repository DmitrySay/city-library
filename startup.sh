#!/bin/bash

#build backend
./gradlew clean assemble

#start db and backend
docker compose --env-file .env.prod up -d
