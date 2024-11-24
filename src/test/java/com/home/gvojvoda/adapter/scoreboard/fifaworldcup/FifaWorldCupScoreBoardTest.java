package com.home.gvojvoda.adapter.scoreboard.fifaworldcup;

import com.home.gvojvoda.adapter.game.football.FootballGameScore;
import com.home.gvojvoda.domain.exception.GameException;
import com.home.gvojvoda.domain.exception.ScoreBoardException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FifaWorldCupScoreBoardTest {

    // -------------------------
    // ---- Initialize Game ----
    // -------------------------

    @ParameterizedTest(name = "Invalid team names on initialize {index}")
    @MethodSource("invalidTeamNamePairs")
    void initializeGame_KO_invalidGameNames(String homeTeamName, String awayTeamName) {
        // Given
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        // When Then
        assertThrows(GameException.class, () -> scoreBoard.initializeGame(homeTeamName, awayTeamName));
    }

    @ParameterizedTest(name = "Team Names already exist {index}")
    @MethodSource("validTeamNamePairsSloItaHun")
    void initializationGame_KO_teamsAlreadyInGame(String homeTeamName, String awayTeamName)
            throws ScoreBoardException, GameException {
        // Given
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        scoreBoard.initializeGame("ITA", "SLO");
        // When Then
        assertThrows(ScoreBoardException.class, () -> scoreBoard.initializeGame(homeTeamName, awayTeamName));
    }

    // ---------------------------
    // ---- Update Game Score ----
    // ---------------------------

    @ParameterizedTest(name = "Invalid team names on update {index}")
    @MethodSource("invalidTeamNamePairsUpdateAndFinish")
    void updateGameScore_KO_invalidTeamNames(String homeTeamName, String awayTeamName) {
        // Given
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        FootballGameScore updateScoreRequest = new FootballGameScore(1, 1);
        // When Then
        assertThrows(GameException.class,
                () -> scoreBoard.updateGameScore(homeTeamName, awayTeamName, updateScoreRequest));
    }

    @ParameterizedTest(name = "Update fail on no match four {index}")
    @MethodSource("validTeamNamePairsSloItaHun")
    void updateGameScore_KO_noGameFound(String homeTeamName, String awayTeamName)
            throws ScoreBoardException, GameException {
        //  Given
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        scoreBoard.initializeGame("GER", "HUN");
        // When Then
        assertThrows(ScoreBoardException.class,
                () -> scoreBoard.updateGameScore(homeTeamName, awayTeamName, new FootballGameScore(4, 2)));
    }

    @Test
    void updateGameScore_OK() throws ScoreBoardException, GameException {
        // GIven
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        scoreBoard.initializeGame("GER", "HUN");
        // When
        scoreBoard.updateGameScore("GER", "HUN", new FootballGameScore(5, 2));
        // Then
        FifaWorldCupScoreBoardGameSummary gameSummary = scoreBoard.getScoreBoardSummary().getFirst();
        assertEquals(5, gameSummary.getHomeTeamScore());
        assertEquals(2, gameSummary.getAwayTeamScore());
    }

    @Test
    void updateGameScore_OK_badNameFormatting() throws ScoreBoardException, GameException {
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        scoreBoard.initializeGame("GER", "HUN");
        scoreBoard.updateGameScore("  gEr", "hUN   ", new FootballGameScore(5, 2));
        FifaWorldCupScoreBoardGameSummary gameSummary = scoreBoard.getScoreBoardSummary().getFirst();
        assertEquals(5, gameSummary.getHomeTeamScore());
        assertEquals(2, gameSummary.getAwayTeamScore());
    }

    // ---------------------------------
    // ---- Get Score Board Summary ----
    // ---------------------------------

    @Test
    void getScoreBoardSummary_OK() throws ScoreBoardException, GameException {
        // Given
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        scoreBoard.initializeGame("MEXICO", "CANADA");
        scoreBoard.initializeGame("SPAIN", "BRAZIL");
        scoreBoard.initializeGame("GERMANY", "FRANCE");
        scoreBoard.initializeGame("URUGUAY", "ITALY");
        scoreBoard.initializeGame("ARGENTINA", "AUSTRALIA");
        scoreBoard.initializeGame("SLOVENIA", "CROATIA");

        scoreBoard.updateGameScore("MEXICO", "CANADA", new FootballGameScore(0, 5));
        scoreBoard.updateGameScore("SPAIN", "BRAZIL", new FootballGameScore(10, 2));
        scoreBoard.updateGameScore("GERMANY", "FRANCE", new FootballGameScore(2, 2));
        scoreBoard.updateGameScore("URUGUAY", "ITALY", new FootballGameScore(6, 6));
        scoreBoard.updateGameScore("SLOVENIA", "CROATIA", new FootballGameScore(1, 1));
        scoreBoard.updateGameScore("ARGENTINA", "AUSTRALIA", new FootballGameScore(3, 1));

        scoreBoard.endGame("SLOVENIA", "CROATIA");

        // When
        List<FifaWorldCupScoreBoardGameSummary> gameSummaryList = scoreBoard.getScoreBoardSummary();
        // Then
        assertEquals(5, gameSummaryList.size());

        assertEquals("URUGUAY", gameSummaryList.getFirst().getHomeTeamName());
        assertEquals("ITALY", gameSummaryList.getFirst().getAwayTeamName());
        assertEquals(6, gameSummaryList.getFirst().getHomeTeamScore());
        assertEquals(6, gameSummaryList.getFirst().getAwayTeamScore());

        assertEquals("SPAIN", gameSummaryList.get(1).getHomeTeamName());
        assertEquals("BRAZIL", gameSummaryList.get(1).getAwayTeamName());
        assertEquals(10, gameSummaryList.get(1).getHomeTeamScore());
        assertEquals(2, gameSummaryList.get(1).getAwayTeamScore());

        assertEquals("MEXICO", gameSummaryList.get(2).getHomeTeamName());
        assertEquals("CANADA", gameSummaryList.get(2).getAwayTeamName());
        assertEquals(0, gameSummaryList.get(2).getHomeTeamScore());
        assertEquals(5, gameSummaryList.get(2).getAwayTeamScore());

        assertEquals("ARGENTINA", gameSummaryList.get(3).getHomeTeamName());
        assertEquals("AUSTRALIA", gameSummaryList.get(3).getAwayTeamName());
        assertEquals(3, gameSummaryList.get(3).getHomeTeamScore());
        assertEquals(1, gameSummaryList.get(3).getAwayTeamScore());

        assertEquals("GERMANY", gameSummaryList.get(4).getHomeTeamName());
        assertEquals("FRANCE", gameSummaryList.get(4).getAwayTeamName());
        assertEquals(2, gameSummaryList.get(4).getHomeTeamScore());
        assertEquals(2, gameSummaryList.get(4).getAwayTeamScore());

    }

    @Test
    void getScoreBoardSummary_OK_noGameInScoreBoard() {
        // Given
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        // When
        List<FifaWorldCupScoreBoardGameSummary> result = scoreBoard.getScoreBoardSummary();
        // Then
        assertEquals(0, result.size());
    }

    // ---------------------
    // ---- Finish Game ----
    // ---------------------

    @ParameterizedTest(name = "Finish Game requests with invalid game names {index}")
    @MethodSource("invalidTeamNamePairsUpdateAndFinish")
    void endGame_KO(String homeTeamName, String awayTeamName) {
        // Given
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        // When Then
        assertThrows(GameException.class, () -> scoreBoard.endGame(homeTeamName, awayTeamName));
    }

    @Test
    void endGame_OK_existingGame() throws GameException, ScoreBoardException {
        // Given
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        scoreBoard.initializeGame("MEXICO", "CANADA");
        scoreBoard.updateGameScore("MEXICO", "CANADA", new FootballGameScore(0, 5));
        scoreBoard.endGame("MEXICO", "CANADA");
        // When Then
        assertEquals(0, scoreBoard.getScoreBoardSummary().size());
    }


    @Test
    void endGame_OK_existingGameBadlyFormatedNames() throws GameException, ScoreBoardException {
        // Given
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        scoreBoard.initializeGame(" Mexico ", "cAnAda");
        scoreBoard.updateGameScore("mexico", "canada", new FootballGameScore(0, 5));
        scoreBoard.endGame("   MexIco ", "CanaDa   ");
        // When Then
        assertEquals(0, scoreBoard.getScoreBoardSummary().size());
    }

    @Test
    void endGame_KO_noGameFound() throws GameException, ScoreBoardException {
        // Given
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        scoreBoard.initializeGame("MEX", "USA");
        scoreBoard.updateGameScore("MEX", "USA", new FootballGameScore(0, 5));
        // When Then
        assertThrows(ScoreBoardException.class, () ->
                scoreBoard.endGame("MEX", "SLO"));
    }

    // -------------------------
    // --- Argument Methods ----
    // -------------------------

    private static Stream<Arguments> validTeamNamePairsSloItaHun() {
        return Stream.of(
                Arguments.of("   SLO   ", "   ITA  "),
                Arguments.of("SLO", "ITA"),
                Arguments.of("SLO", "HUN"),
                Arguments.of("ITA", "SLO"),
                Arguments.of("ITA", "HUN"),
                Arguments.of("HUN", "SLO"),
                Arguments.of("HUN", "ITA"));
    }

    private static Stream<Arguments> invalidTeamNamePairs() {
        return Stream.of(
                Arguments.of(null, "Slovenia"),
                Arguments.of("Slovenia", null),
                Arguments.of(null, null),
                Arguments.of("   ", "         "),
                Arguments.of("Slo%Venia", "Italia"),
                Arguments.of("Slovenia", "Ital;i"),
                Arguments.of("SLO", "SLO"),
                Arguments.of("SLO", "   SLO    "));
    }

    private static Stream<Arguments> invalidTeamNamePairsUpdateAndFinish() {
        return Stream.of(
                Arguments.of(null, "Slovenia"),
                Arguments.of("Slovenia", null),
                Arguments.of(null, null),
                Arguments.of("   ", "         "),
                Arguments.of("Slo%Venia", "Italia"),
                Arguments.of("Slovenia", "Ital;i"));
    }

}
