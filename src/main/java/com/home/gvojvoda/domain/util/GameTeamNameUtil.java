package com.home.gvojvoda.domain.util;

import com.home.gvojvoda.domain.exception.GameException;

import java.util.Objects;
import java.util.regex.Pattern;

public class GameTeamNameUtil {

    private static final String TEAM_NAME_VALIDATION_REGEXP = "^[\\w\\s]{1,100}$";

    private GameTeamNameUtil() {
    }

    public static String validateAndFormatTeamName(String teamName) throws GameException {
        validateTeamName(teamName);
        return teamName.trim().replaceAll("\\s+", "_").toUpperCase();
    }

    private static void validateTeamName(String teamName) throws GameException {
        if (Objects.isNull(teamName) || teamName.isBlank()) throw new GameException("Team Name cannot be null");
        Pattern patter = Pattern.compile(TEAM_NAME_VALIDATION_REGEXP);
        if (!patter.matcher(teamName).matches()) throw new GameException("Team name contains invalid characters");
    }
}
