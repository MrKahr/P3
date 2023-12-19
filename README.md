# P3
## Prerequisites:
JDK 17 or above
<br>
Maven and Maven Wrapper installed
<br>
MySQL Server installed

### MySQL database created using the following commands:
create database db_dummy;
<br>
create user 'DevDungeon'@'%' identified by 'DiceNData';
<br>
grant all on db_dummy.* to 'DevDungeon'@'%';

#### To start the server:
./mvnw spring-boot:run

#### To run tests:
./mvnw test