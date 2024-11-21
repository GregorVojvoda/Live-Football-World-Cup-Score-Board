package com.home.gvojvoda.adapter.game.football;

import java.time.LocalDateTime;

import com.home.gvojvoda.domain.exception.GameException;
import com.home.gvojvoda.domain.port.Game;

import lombok.Getter;

@Getter
public class FootballGame implements Game<FootballGameScoreUpdateRequest>{

    private final LocalDateTime matchStart;
    private final String homeTeamName;
    private final String awayTeamName;
    private int homeScore;
    private int awayScore;
    private boolean finish;

    public FootballGame(String homeTeam, String awayTeam) throws GameException {
        this.matchStart = LocalDateTime.now();
        this.homeTeamName = FootballGameTeamNameUtil.validateAndFormatTeamName(homeTeam);
        this.awayTeamName = FootballGameTeamNameUtil.validateAndFormatTeamName(awayTeam);
        this.homeScore = 0;
        this.awayScore = 0;
        this.finish = false;
    }

    public void finishMatch() {
        this.finish = true;
    }

    @Override
    public void updateScore(FootballGameScoreUpdateRequest request) throws GameException {
        if(this.finish) throw new GameException("Finished match score cannot be modified");
        if(request.getUpdatedHomeTeamScore() < 0 
        || request.getUpdatedAwayTeamScore() < 0
        || request.getUpdatedHomeTeamScore() > 200
        || request.getUpdatedAwayTeamScore() > 200) throw new GameException("Score value cannot be negative or higher that 200");
        this.homeScore = request.getUpdatedHomeTeamScore();
        this.awayScore = request.getUpdatedAwayTeamScore();
    }

    @Override
    public long getOverallScore() {
        return (homeScore + awayScore);
    }


}
