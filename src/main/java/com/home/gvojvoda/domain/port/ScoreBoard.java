package com.home.gvojvoda.domain.port;

import com.home.gvojvoda.domain.exception.GameException;
import com.home.gvojvoda.domain.exception.ScoreBoardException;

public interface ScoreBoard<T, A> {

    void initializeGame(String homeTeam, String awayTeam) throws ScoreBoardException, GameException;

    void endGame(String homeTeam, String awayTeam) throws ScoreBoardException, GameException;

    void updateGameScore(String homeTeamName, String awayTeamName, A score)
            throws ScoreBoardException, GameException;

    T getScoreBoardSummary();

}