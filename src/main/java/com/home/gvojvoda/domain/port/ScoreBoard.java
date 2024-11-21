package com.home.gvojvoda.domain.port;

import com.home.gvojvoda.domain.exception.GameException;
import com.home.gvojvoda.domain.exception.ScoreBoardException;

public interface ScoreBoard {

    void initializeGame(String homeTeam, String awayTeam) throws ScoreBoardException, GameException;

}
