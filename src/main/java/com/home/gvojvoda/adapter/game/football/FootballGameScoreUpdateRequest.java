package com.home.gvojvoda.adapter.game.football;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FootballGameScoreUpdateRequest {
    private final int updatedHomeTeamScore;
    private final int updatedAwayTeamScore;

}
