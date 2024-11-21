package com.home.gvojvoda.adapter.scoreboard.fifaworldcup;

import com.home.gvojvoda.adapter.game.football.FootballGame;
import lombok.Getter;

@Getter
public class FifaWorldCupScoreBoardGameSummary {

    private final String homeTeamName;
    private final String awayTeamName;
    private final int homeTeamScore;
    private final int awayTeamScore;

    public FifaWorldCupScoreBoardGameSummary(FootballGame game) {
        this.homeTeamName = game.getHomeTeamName();
        this.awayTeamName = game.getAwayTeamName();
        this.homeTeamScore = game.getHomeScore();
        this.awayTeamScore = game.getAwayScore();
    }

}
