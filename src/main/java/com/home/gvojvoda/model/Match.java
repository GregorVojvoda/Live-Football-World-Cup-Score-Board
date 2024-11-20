package com.home.gvojvoda.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

import com.home.gvojvoda.exception.MatchException;

import lombok.Getter;

@Getter
public class Match {

    private static final String TEAM_NAME_VALIDATION_REGEXP = "^[\\w\\s]{1,100}$";

    private final LocalDateTime matchStart;
    private final String homeTeamName;
    private final String awayTeamName;
    private int homeScore;
    private int awayScore;
    private boolean finish;

    public Match(String homeTeam, String awayTeam) throws MatchException {
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

    private void validateTeamName(String teamName) throws MatchException{
        if(Objects.isNull(teamName)) throw new MatchException("Team Name cannot be null");
        Pattern patter = Pattern.compile(TEAM_NAME_VALIDATION_REGEXP);
        if(!patter.matcher(teamName).matches()) throw new MatchException("Team name contains invalid characters"); 
    }

    public void finishMatch() {
        this.finish = true;
    }
}
