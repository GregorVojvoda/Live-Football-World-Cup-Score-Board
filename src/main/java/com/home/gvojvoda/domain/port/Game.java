package com.home.gvojvoda.domain.port;

import com.home.gvojvoda.domain.exception.GameException;

public interface Game<T> {

    void endGame();

    void updateScore(T scoreUpdateRequest) throws GameException;

    long getOverallScore();

}
