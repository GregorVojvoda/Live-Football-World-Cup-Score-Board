# Live Score Board

## Table of Content

- [Introduction](#introduction)
- [Technologies](#technologies)
- [Overview](#overview)
- [Features](#features)
  - [FootballGame](#footballgame)
    - [Updating the score](#updating-the-score)
    - [Ending the Game](#ending-the-game)
    - [Getting the Overall Score](#getting-the-overall-score)
  - [FifaWorldCupScoreBoard](#fifaworldcupscoreboard)
    - [Initializing a Game](#initializing-a-game)
    - [Updating a Game Score](#updating-a-game-score)
    - [Getting the Score Board Summary](#getting-the-score-board-summary)
    - [End A Game](#end-a-game)

----
## Introduction
The Live Football World Cup Scoreboard is an app designed to track the status of active sports games on a scoreboard. \
Currently, it features an implementation for the FIFA World Cup, focusing on football games. However, it can be easily extended to support scoreboards for other championships and sports.

----
## Technologies
![](https://skillicons.dev/icons?i=java)
![](https://skillicons.dev/icons?i=maven)
> Java 21

----
## Overview
### ScoreBoard interface  
- Initialize a game inside the ScoreBoard
    ```java
      void initializeGame(String homeTeam, String awayTeam) throws ScoreBoardException, GameException;
    ```
- Update the score of an active game inside the ScoreBoard 
    ```java
    void endGame(String homeTeam, String awayTeam) throws ScoreBoardException, GameException;
    ```
- End a game inside the ScoreBoard 
    ```java
    void updateGameScore(String homeTeamName, String awayTeamName, A scoreUpdateRequest)
                throws ScoreBoardException, GameException;
    ```
- Return an overview of the active games in the ScoreBoard 
    ```java
    T getScoreBoardSummary();
    ```

### The Game interface
- End the active game
    ```java 
     void endGame();
    ```
- Update the score of the game
    ```java
     void updateScore(T scoreUpdateRequest) throws GameException;
    ```
- Get the overall game score
    ```java
     long getOverallScore();
    ```
  
----
## Features
At the moment there is a FootBallGame class implementation of the Game interface and FifaWorldCupScoreBoard implementation of the ScoreBoard interface.
### FootBallGame
The FootballGame is defined by six fields 
- matchStart -> A ```LocalDateTime``` object that is final and holds the initialization date and time.
- homeTeamName -> The name of the home team, matching the regular expression: ```(^[A-Z0-9_]{1,100}$)```
- awayTeamName -> The name of the away team, matching the same regular expression: ```(^[A-Z0-9_]{1,100}$)```
- homeScore -> An integer representing the home team’s score, constrained by: ```(0 <= n <=200 )```
- awayScore -> An integer representing the away team’s score, constrained by: ```(0 <= n <=200 )```
- finis -> A boolean indicating whether the game is active (false) or finished (true).

The class can be initialized by providing two team names that match the regular expression: ```(^\w\s]{1,100}$)```. \
The names are then formatted. The formatting consist in:
1. Trimming leading and trailing whitespace.
2. Replacing multiple spaces with a single underscore (_).
3. Converting all characters to uppercase.

If the provided strings do not match the regular expression or if the two names are identical after formatting, the initialization fails with a ```GameException```.

```java
public FootballGame(String homeTeam, String awayTeam) throws GameException;
```
#### Updating the Score:
You can update the score using a FootballGameScore object.
The provided new scores replace the current ones, but they must satisfy the following conditions:
- Scores cannot be negative.
- Scores cannot exceed 200.
  
The upper limit prevents issues in overall score calculations and reflects the unlikely scenario of a football match reaching such high scores.
Lower scores than the current ones are allowed, as goals can be revoked in rare situations.
#### Ending the Game:
  You can end the game, which simply sets the finish property to true.
#### Getting the Overall Score:
  The overall score of the game is the sum of the home and away team scores.

### FifaWorldCupScoreBoard
The **FifaWorldCupScoreBoard** is a simple implementation of the ScoreBoard interface. It does not require any initialization parameters.
Upon creation, it simply creates an empty ```Map<String, FootballGame>```.
```java
public FifaWorldCupScoreBoard();
```
#### Initializing a Game
Initializing a game in the scoreboard requires two valid team names (home and away) *(see FootballGame team name validation)*. The names are then formatted using the ```FootballGame``` formatting logic.
Next, we check the scoreboard map to see if it contains any active game with either of the provided and formatted team names. If a provided team name is found in the scoreboard, a ```ScoreBoardException``` is thrown.
If both validations pass, a key for the game is created by combining the formatted home team name and formatted away team name, separated by a dash (-).
A new ```FootballGame``` is then initialized and inserted into the map with the generated key.
#### Updating a Game Score
To update a game score, two valid team names *(home and away)* and a ```FootballGameScore``` object are required.
The team names are validated and formatted in the same way as before. Then, we construct the key in the same manner as in the initializeGame function.
Next, we search the map using the generated key. If no value is found with the key, a ScoreBoardException is thrown. Otherwise, we attempt to update the found FootballGame with the provided ```FootballGameScore```.
If the update fails, a ```GameException``` is thrown.
#### End A Game
To end a game, two valid team names *(home and away)* are required *(see FootballGame team name validation)*. The names are then formatted using the FootballGame formatting logic and used to construct the key as before.
Using the key, we search the map. If no game is found with the key, a ```ScoreBoardException``` is thrown.
Otherwise, the FootballGame is ended and removed from the map.

#### Getting the Score Board Summary
The scoreboard summary returns an ordered list of the games in the map.
The games are primarily sorted by the result of the ```getOverallScore()``` function, with higher-scoring games prioritized.
If two games have the same overall score, they are sorted by the creation date, with the most recent games prioritized.
The sorted games are then returned as an ordered list of ```FifaWorldCupScoreBoardGameSummary``` class objects. This is done to provide only the necessary information in the response.
The information includes the team names and their corresponding scores.





