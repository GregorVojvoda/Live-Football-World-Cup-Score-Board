package com.home.gvojvoda.adapter.game.football;

import com.home.gvojvoda.domain.exception.GameException;
import com.home.gvojvoda.domain.port.Game;
import com.home.gvojvoda.domain.util.GameTeamNameProcessor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class FootballGame implements Game<FootballGameScore> {

    private final LocalDateTime matchStart;
    private final String homeTeamName;
    private final String awayTeamName;
    private int homeScore;
    private int awayScore;
    private boolean finish;

    private final GameTeamNameProcessor gameTeamNameProcessor = new GameTeamNameProcessor();

    public FootballGame(String homeTeam, String awayTeam) throws GameException {
        String finalHomeTeamName = gameTeamNameProcessor.validateAndFormatTeamName(homeTeam);
        String finalAwayTeamName = gameTeamNameProcessor.validateAndFormatTeamName(awayTeam);
        if (finalHomeTeamName.equals(finalAwayTeamName)) throw new GameException("Team names cannot be equal");
        this.matchStart = LocalDateTime.now();
        this.homeTeamName = finalHomeTeamName;
        this.awayTeamName = finalAwayTeamName;
        this.homeScore = 0;
        this.awayScore = 0;
        this.finish = false;
    }

    @Override
    public void endGame() {
        this.finish = true;
    }

    @Override
    public void updateScore(FootballGameScore request) throws GameException {
        if (this.finish) throw new GameException("Finished match score cannot be modified");
        if (Objects.isNull(request)) throw new GameException("FootballGameScoreUpdateRequest request cannot be null");
        if (request.homeTeamScore() < 0
                || request.awayTeamScore() < 0
                || request.homeTeamScore() > 200
                || request.awayTeamScore() > 200)
            throw new GameException("Score value cannot be negative or higher that 200");
        this.homeScore = request.homeTeamScore();
        this.awayScore = request.awayTeamScore();
    }
}
