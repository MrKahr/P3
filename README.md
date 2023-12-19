# P3
## Prerequisites:
JDK 17 or above
<br>
Maven version 3.9.5 or newer and Maven Wrapper installed
<br>
MySQL Server 8.0.25 or newer installed and currently running

### MySQL database created using the following commands in the MySQL command line client:
create database db_dummy;
<br>
create user 'DevDungeon'@'%' identified by 'DiceNData';
<br>
grant all on db_dummy.* to 'DevDungeon'@'%';

#### To start the server, set your current directory to .\BackendNServer and run the following commands:
./mvnw spring-boot:run

#### To run tests:
./mvnw test
