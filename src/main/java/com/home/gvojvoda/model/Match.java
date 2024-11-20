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
    private boolean ended;

    private Match(String homeTeam, String awayTeam) {
        this.matchStart = LocalDateTime.now();
        this.homeTeamName = homeTeam;
        this.awayTeamName = awayTeam;
        this.homeScore = 0;
        this.awayScore = 0;
        this.ended = false;
    }

    public static Match init(String homeTeam, String awayTeam) throws MatchException {
        validateTeamName(homeTeam);
        validateTeamName(awayTeam);
        return new Match(modifyTeamName(homeTeam), modifyTeamName(awayTeam));
    }

    private static String modifyTeamName(String teamName) {
        return teamName.trim().replaceAll("\\s+", "_").toUpperCase();
        
    }

    private static void validateTeamName(String teamName) throws MatchException{
        if(Objects.isNull(teamName)) throw new MatchException("Team Name cannot be null");
        Pattern patter = Pattern.compile(TEAM_NAME_VALIDATION_REGEXP);
        if(!patter.matcher(teamName).matches()) throw new MatchException("Team name contains invalid characters"); 
    }
}
