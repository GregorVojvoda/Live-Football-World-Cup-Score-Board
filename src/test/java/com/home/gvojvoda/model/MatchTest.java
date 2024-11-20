package com.home.gvojvoda.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.home.gvojvoda.exception.MatchException;

class MatchTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("matchesWithInvalidNames")
    void matchInit_KO_invalidNames(String testName, String homeTeam, String awaiTeam) {
        assertThrows(MatchException.class, () -> {
            Match.init(homeTeam, awaiTeam);
        });
    }

    private static Stream<Arguments> matchesWithInvalidNames() {
        return Stream.of(
            Arguments.of("HomeTeamNull", null, "Slovenia"),
            Arguments.of("AwayTeamNull", "Slovenia", null),
            Arguments.of("BothTeamsNull", null, null),
            Arguments.of("InvalidCharactersHomeTeam", "Slo%Venia", "Italia"),
            Arguments.of("InvalidCharactersAwayTeam", "Slovenia", "Ital;i")
        );
    }

    @ParameterizedTest(name = "{0} & {2}")
    @MethodSource("matchesWithValidTeamNames")
    void matchInit_OK(String providedHomeName, String finalHomeName, String providedAwayName, String finalAwayName) throws MatchException {
        // Given When
        Match match = Match.init(providedHomeName, providedAwayName);

        // Then 
        assertEquals(finalHomeName, match.getHomeTeamName());
        assertEquals(finalAwayName, match.getAwayTeamName());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
        assertFalse(match.isEnded());
        assertNotNull(match.getMatchStart());
    }

    private static Stream<Arguments> matchesWithValidTeamNames() {
        return Stream.of(
            Arguments.of("    Slovenia    ", "SLOVENIA", "Italia", "ITALIA"),
            Arguments.of("Slovenia 1", "SLOVENIA_1", "Italia       1", "ITALIA_1"),
            Arguments.of("SLOVENIA_01", "SLOVENIA_01", "ITA    LIA 01", "ITA_LIA_01")
        );
    }

    
}
