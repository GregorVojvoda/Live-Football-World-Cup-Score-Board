# Live Football World Cup Score Board

## Game 
There is a generic game interface that we have currently implemented for a Football game. \
As far as I can immagine if we would like to implement an other type of game _(like tennis, basketball, ...)_ the only difference is the scoring sistem. \
There fore the Game interface defines a generic as a ```updateScore(T)``` request type. 

## Football Game

## FoodballGameTeamNameUtil
The team names defined for the game are formatted in a generic way. \
This is to help us with the initialization of new games in the ScoreBoard. \
A ScoreBoard cannot initalize a new game with a tem _(team name)_ that is already in an active game. With a general naming convention of the team names, we can later pair team names as *Slovenia* and *SLOVENIA* _(one of many possibilityes)_ as equals.