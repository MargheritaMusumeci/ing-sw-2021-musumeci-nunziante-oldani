# ing-sw-2021-musumeci-nunziante-oldani
## Master of Renaissance
![alt text](https://github.com/matteoldani/ing-sw-2021-musumeci-nunziante-oldani/blob/master/deliverables/title.png?raw=true)
Masters of Renaissance is a game with simple rules offering deep strategic choices of action selection and engine building.

In Masters of Renaissance you are an important citizen of Florence and your goal is to increase your fame and prestige. Take resources from the market and use them to buy new cards. Expand your power both in the city and in the surrounding territories! Every card gives you a production power that transforms the resoruces so you can store them in our strongbox. Try to use the Leaders' abilities to your advantage and don't forget to show your devotion to the Pope!

## Index

- Group components
- External Libraries and Tools
- Features implemented
- Instructions


## Group Components

| Cognome | Nome | e-mail | Matricola | Codice Persona
| ------ | ------ |----- |----- |----- |
| Musumeci | Margherita| margherita.musumeci@mail.polimi.it| 907435| 10600069
| Nunziante |  Matteo| matteo.nunziante@mail.polimi.it | 913670 | 10670132
| Oldani |Matteo| matteo.oldani@mail.polimi.it  | 910756 | 10620207

## External libraries and tools

- javafx-fxml version 16
- javafx-controls version 16
- junit version 4.11
- gson version 2.8.5
- maven shade plugin version 3.2.4
- fxlm files created with scene builder

## Features Implemented

- Full rules
- CLI
- GUI
- Socket Connection
- Advanced features:
    - Multi game 
    - Resilience to Disconnections
    - Persistance

## Instructions

#### Summary: 
- server.jar has no options and handles the server of this game 
- client.jar has 1 possible option
    - cli if you want to play with the cli interface 
    - gui if you want to play with the gui interface

#### More in details:

Into the deliverables folder of this poject can be found two jars. 
The one named "server.jar" is responsible to the server of this game and can be executed with:

```sh
java -jar server.jar
```
It has no options and it will require the port after its execution. You are not able to interact with the server.

The second jar, "client.jar", is the executable dedicated to the clients. Here you can choose between a GUI interface or a CLI one. This selection clould be made through arguments. 

With the command: 
```sh
java -jar client.jar gui
```
you will launch the GUI version of the application.

With the command: 
```sh
java -jar client.jar cli
```
you will launch the CLI version of the application.

If no argumments is specified the application will load the GUI version by default.

After the execution the client will ask you for the server ip address and port to connect.

#### Disconnections
If a disconnection occurs you can rejoin the unless its finished. To do that you have to restart the client jar and reconnect to the same server and insert the same number of player after have chosen your priviuos nickname.


