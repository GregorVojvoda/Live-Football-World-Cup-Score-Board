package com.home.gvojvoda.adapter.game.football;

import com.home.gvojvoda.domain.exception.GameException;
import com.home.gvojvoda.domain.port.Game;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FootballGame implements Game<FootballGameScoreUpdateRequest> {

    private final LocalDateTime matchStart;
    private final String homeTeamName;
    private final String awayTeamName;
    private int homeScore;
    private int awayScore;
    private boolean finish;

    public FootballGame(String homeTeam, String awayTeam) throws GameException {
        String finalHomeTeamName = FootballGameTeamNameUtil.validateAndFormatTeamName(homeTeam);
        String finalAwayTeamName = FootballGameTeamNameUtil.validateAndFormatTeamName(awayTeam);
        if (finalHomeTeamName.equals(finalAwayTeamName)) throw new GameException("Team names cannot be equal");
        this.matchStart = LocalDateTime.now();
        this.homeTeamName = finalHomeTeamName;
        this.awayTeamName = finalAwayTeamName;
        this.homeScore = 0;
        this.awayScore = 0;
        this.finish = false;
    }

    public void finishMatch() {
        this.finish = true;
    }

    @Override
    public void updateScore(FootballGameScoreUpdateRequest request) throws GameException {
        if (this.finish) throw new GameException("Finished match score cannot be modified");
        if (request.updatedHomeTeamScore() < 0
                || request.updatedAwayTeamScore() < 0
                || request.updatedHomeTeamScore() > 200
                || request.updatedAwayTeamScore() > 200)
            throw new GameException("Score value cannot be negative or higher that 200");
        this.homeScore = request.updatedHomeTeamScore();
        this.awayScore = request.updatedAwayTeamScore();
    }

    @Override
    public long getOverallScore() {
        return (homeScore + awayScore);
    }


}
