package com.home.gvojvoda.adapter.game.football;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.home.gvojvoda.domain.exception.GameException;

class FootballGameTest {

    // Match initialization

    @ParameterizedTest(name = "{0}")
    @MethodSource("matchesWithInvalidNames")
    void matchInit_KO_invalidNames(String testName, String homeTeam, String awaiTeam) {
        assertThrows(GameException.class, () -> {
            new FootballGame(homeTeam, awaiTeam);
        });
    }

    private static Stream<Arguments> matchesWithInvalidNames() {
        return Stream.of(
            Arguments.of("HomeTeamNull", null, "Slovenia"),
            Arguments.of("AwayTeamNull", "Slovenia", null),
            Arguments.of("BothTeamsNull", null, null),
            Arguments.of("BothTeamsAreBlank", "   ", "         "),
            Arguments.of("InvalidCharactersHomeTeam", "Slo%Venia", "Italia"),
            Arguments.of("InvalidCharactersAwayTeam", "Slovenia", "Ital;i")
        );
    }

    @ParameterizedTest(name = "{0} & {2}")
    @MethodSource("matchesWithValidTeamNames")
    void matchInit_OK(String providedHomeName, String finalHomeName, String providedAwayName, String finalAwayName) throws GameException {
        // Given When
        FootballGame match = new FootballGame(providedHomeName, providedAwayName);

        // Then 
        assertEquals(finalHomeName, match.getHomeTeamName());
        assertEquals(finalAwayName, match.getAwayTeamName());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
        assertFalse(match.isFinish());
        assertNotNull(match.getMatchStart());
    }

    private static Stream<Arguments> matchesWithValidTeamNames() {
        return Stream.of(
            Arguments.of("    Slovenia    ", "SLOVENIA", "Italia", "ITALIA"),
            Arguments.of("Slovenia 1", "SLOVENIA_1", "Italia       1", "ITALIA_1"),
            Arguments.of("SLOVENIA_01", "SLOVENIA_01", "ITA    LIA 01", "ITA_LIA_01")
        );
    }

    // FinishMatch

    @Test
    void finishMatch_OK() throws GameException {
        // Given 
        FootballGame match = new FootballGame("SLO", "ITA");
        match.finishMatch();
        assertTrue(match.isFinish());
    }

    // Update Score

    @Test
    void updateScore_KO_machIsOver() throws GameException {
        // Given
        FootballGame match = new FootballGame("SLO", "ITA");
        match.finishMatch();
        // Then
        assertThrows(GameException.class, () -> match.updateScore(new FootballGameScoreUpdateRequest(1, 1)));
    }

    @ParameterizedTest
    @MethodSource("negativeScoreCombiationions")
    void updateScore_KO_scoreIsNegative(FootballGameScoreUpdateRequest request) throws GameException {
        // Given
        FootballGame match = new FootballGame("SLO", "ITA");
        // Then
        assertThrows(GameException.class, () -> match.updateScore(request));
    }

    private static Stream<Arguments> negativeScoreCombiationions(){
        return Stream.of(
            Arguments.of(new FootballGameScoreUpdateRequest(0, -1)),
            Arguments.of(new FootballGameScoreUpdateRequest(-1, 0)),
            Arguments.of(new FootballGameScoreUpdateRequest(-1, -1))
        );
    }

    @Test
    void updateScore_OK() throws GameException {
        FootballGame match = new FootballGame("SLO", "ITA");
        match.updateScore(new FootballGameScoreUpdateRequest(2, 3));
        assertEquals(2, match.getHomeScore());
        assertEquals(3, match.getAwayScore());
    }

    
}
