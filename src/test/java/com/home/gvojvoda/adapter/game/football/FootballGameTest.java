package com.home.gvojvoda.adapter.game.football;

import com.home.gvojvoda.domain.exception.GameException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FootballGameTest {

    // ------------------------------
    // ---- Match initialization ----
    // ------------------------------

    @ParameterizedTest(name = "{0}")
    @MethodSource("gamesWithInvalidNames")
    void gameInit_KO_invalidNames(String testName, String homeTeam, String awayTeam) {
        assertThrows(GameException.class, () -> {
            new FootballGame(homeTeam, awayTeam);
        });
    }

    private static Stream<Arguments> gamesWithInvalidNames() {
        return Stream.of(
                Arguments.of("HomeTeamNull", null, "Slovenia"),
                Arguments.of("AwayTeamNull", "Slovenia", null),
                Arguments.of("BothTeamsNull", null, null),
                Arguments.of("BothTeamsAreBlank", "   ", "         "),
                Arguments.of("InvalidCharactersHomeTeam", "Slo%Venia", "Italia"),
                Arguments.of("InvalidCharactersAwayTeam", "Slovenia", "Ital;i"),
                Arguments.of("SameName1", "SLO", "SLO"),
                Arguments.of("SameName2", "SLO", "   SLO    ")
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

    // ----------------------
    // ---- Finish Match ----
    // ----------------------

    @Test
    void endGame_OK() throws GameException {
        // Given 
        FootballGame match = new FootballGame("SLO", "ITA");
        match.endGame();
        assertTrue(match.isFinish());
    }
    // ----------------------
    // ---- Update Score ----
    // ----------------------

    @Test
    void updateScore_KO_machIsOver() throws GameException {
        // Given
        FootballGame match = new FootballGame("SLO", "ITA");
        match.endGame();
        // Then
        assertThrows(GameException.class, () -> match.updateScore(new FootballGameScore(1, 1)));
    }

    @ParameterizedTest
    @MethodSource("negativeScoreCombinations")
    void updateScore_KO_scoreIsNegative(FootballGameScore request) throws GameException {
        // Given
        FootballGame match = new FootballGame("SLO", "ITA");
        // Then
        assertThrows(GameException.class, () -> match.updateScore(request));
    }

    @Test
    void updateScore_KO_nullFootballGameScore() throws GameException {
        // Given
        FootballGame match = new FootballGame("SLO", "ITA");
        // Then
        assertThrows(GameException.class, () -> match.updateScore(null));
    }

    @Test
    void updateScore_OK() throws GameException {
        // Given
        FootballGame match = new FootballGame("SLO", "ITA");
        // When
        match.updateScore(new FootballGameScore(2, 3));
        // Then
        assertEquals(2, match.getHomeScore());
        assertEquals(3, match.getAwayScore());
    }

    // ------------------------
    // ---- Get TotalScore ----
    // ------------------------

    @Test
    void getTotalScore_OK() throws GameException {
        // Given
        FootballGame game = new FootballGame("ITA", "SLO");
        // When Then
        assertEquals(0, game.getOverallScore());
    }

    @Test
    void getTotalScore_OK_withScoreUpdate() throws GameException {
        // Given 
        FootballGame game = new FootballGame("ITA", "SLO");
        game.updateScore(new FootballGameScore(5, 3));
        // When Then
        assertEquals(8, game.getOverallScore());
    }

    @Test
    void getTotalScore_OK_finishedGame() throws GameException {
        // Given
        FootballGame game = new FootballGame("SLO", "ITA");
        game.updateScore(new FootballGameScore(6, 6));
        game.endGame();
        // When Then
        assertEquals(12, game.getOverallScore());

    }

    // -------------------------
    // --- Argument Methods ----
    // -------------------------

    private static Stream<Arguments> matchesWithValidTeamNames() {
        return Stream.of(
                Arguments.of("    Slovenia    ", "SLOVENIA", "Italia", "ITALIA"),
                Arguments.of("Slovenia 1", "SLOVENIA_1", "Italia       1", "ITALIA_1"),
                Arguments.of("SLOVENIA_01", "SLOVENIA_01", "ITA    LIA 01", "ITA_LIA_01")
        );
    }

    private static Stream<Arguments> negativeScoreCombinations() {
        return Stream.of(
                Arguments.of(new FootballGameScore(0, -1)),
                Arguments.of(new FootballGameScore(-1, 0)),
                Arguments.of(new FootballGameScore(-1, -1)),
                Arguments.of(new FootballGameScore(201, 3)),
                Arguments.of(new FootballGameScore(6, 300)),
                Arguments.of(new FootballGameScore(5000, 999999))
        );
    }


}
