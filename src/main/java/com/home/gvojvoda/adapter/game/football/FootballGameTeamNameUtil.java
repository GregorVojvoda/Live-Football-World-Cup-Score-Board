package com.home.gvojvoda.adapter.game.football;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.home.gvojvoda.domain.exception.GameException;

public class FootballGameTeamNameUtil {

    private static final String TEAM_NAME_VALIDATION_REGEXP = "^[\\w\\s]{1,100}$";

    private FootballGameTeamNameUtil() {}

    public static String validateAndFormatTeamName(String teamName) throws GameException {
        validateTeamName(teamName);
        return teamName.trim().replaceAll("\\s+", "_").toUpperCase();
    }

    private static void validateTeamName(String teamName) throws GameException {
        if(StringUtils.isBlank(teamName)) throw new GameException("Team Name cannot be null");
        Pattern patter = Pattern.compile(TEAM_NAME_VALIDATION_REGEXP);
        if(!patter.matcher(teamName).matches()) throw new GameException("Team name contains invalid characters"); 
    }
}
