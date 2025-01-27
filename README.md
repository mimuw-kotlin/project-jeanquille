[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/M0kyOMLZ)
# Project Jeanquille

## Authors
- Stanisław Struzik
- Maciej Curulak

## Description
Project Jeanquille is a managing app, where users can create groups and settle debts with friends.

## Features
- accounts
- creating groups
- adding group members
- friends
- adding new bills
- summing up group expenses
- calculating debts

## Plan
In the first part we're going to implement backend.

In the second part we're going to implement frontend.

## Frameworks and libraries
- Spring Boot
- Compose for Desktop
- Ktor
 
## Preqrequisites
- Java 21
- docker-compose

## How to run
```
cd docker
docker-compose up -d
cd ../backend
./gradlew build
./gradlew bootRun
cd ../frontend
./gradlew build
./gradlew run
```

