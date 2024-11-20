package com.home.gvojvoda.adapter.game.football;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

import com.home.gvojvoda.domain.exception.GameException;
import com.home.gvojvoda.domain.port.Game;

import lombok.Getter;

@Getter
public class FootballGame implements Game<FootballGameScoreUpdateRequest>{

    private static final String TEAM_NAME_VALIDATION_REGEXP = "^[\\w\\s]{1,100}$";

    private final LocalDateTime matchStart;
    private final String homeTeamName;
    private final String awayTeamName;
    private int homeScore;
    private int awayScore;
    private boolean finish;

    public FootballGame(String homeTeam, String awayTeam) throws GameException {
        validateTeamName(homeTeam);
        validateTeamName(awayTeam);
        this.matchStart = LocalDateTime.now();
        this.homeTeamName = modifyTeamName(homeTeam);
        this.awayTeamName = modifyTeamName(awayTeam);
        this.homeScore = 0;
        this.awayScore = 0;
        this.finish = false;
    }

    private String modifyTeamName(String teamName) {
        return teamName.trim().replaceAll("\\s+", "_").toUpperCase();
    }

    private void validateTeamName(String teamName) throws GameException{
        if(Objects.isNull(teamName)) throw new GameException("Team Name cannot be null");
        Pattern patter = Pattern.compile(TEAM_NAME_VALIDATION_REGEXP);
        if(!patter.matcher(teamName).matches()) throw new GameException("Team name contains invalid characters"); 
    }

    public void finishMatch() {
        this.finish = true;
    }

    public void updateScore(FootballGameScoreUpdateRequest request) throws GameException {
        if(this.finish) throw new GameException("Finished match score cannot be modified");
        if(request.getUpdatedHomeTeamScore() < 0 || request.getUpdatedAwayTeamScore() < 0) throw new GameException("Score value cannot be negative");
        this.homeScore = request.getUpdatedHomeTeamScore();
        this.awayScore = request.getUpdatedAwayTeamScore();
    }
}
