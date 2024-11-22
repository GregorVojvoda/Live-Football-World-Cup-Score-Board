package com.home.gvojvoda.adapter.scoreboard.fifaworldcup;

import com.home.gvojvoda.adapter.game.football.FootballGameScoreUpdateRequest;
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

    // initializeGame
    @ParameterizedTest(name = "Invalid team names on initialize {index}")
    @MethodSource("invalidTeamNamePairs")
    void initializeGame_KO_invalidGameNames(String homeTeamName, String awayTeamName) {
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        assertThrows(GameException.class, () -> scoreBoard.initializeGame(homeTeamName, awayTeamName));
    }

    @ParameterizedTest(name = "Team Names already exist {index}")
    @MethodSource("validTeamNamePairsSloItaHun")
    void initializationGame_KO_teamsAlreadyInGame(String homeTeamName, String awayTeamName)
            throws ScoreBoardException, GameException {
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        scoreBoard.initializeGame("ITA", "SLO");
        assertThrows(ScoreBoardException.class, () -> scoreBoard.initializeGame(homeTeamName, awayTeamName));
    }

    // updateGameScore

    @ParameterizedTest(name = "Invalid team names on update {index}")
    @MethodSource("invalidTeamNamePairsUpdateAndFinish")
    void updateGameScore_KO_invalidTeamNames(String homeTeamName, String awayTeamName) {
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        FootballGameScoreUpdateRequest updateScoreRequest = new FootballGameScoreUpdateRequest(1, 1);
        assertThrows(GameException.class,
                () -> scoreBoard.updateGameScore(homeTeamName, awayTeamName, updateScoreRequest));
    }

    @ParameterizedTest(name = "Update fail on no match four {index}")
    @MethodSource("validTeamNamePairsSloItaHun")
    void updateGameScore_KO_noGameFound(String homeTeamName, String awayTeamName)
            throws ScoreBoardException, GameException {
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        scoreBoard.initializeGame("GER", "HUN");
        assertThrows(ScoreBoardException.class,
                () -> scoreBoard.updateGameScore(homeTeamName, awayTeamName, new FootballGameScoreUpdateRequest(4, 2)));
    }

    @Test
    void updateGameScore_OK() throws ScoreBoardException, GameException {
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        scoreBoard.initializeGame("GER", "HUN");
        scoreBoard.updateGameScore("GER", "HUN", new FootballGameScoreUpdateRequest(5, 2));
        FifaWorldCupScoreBoardGameSummary gameSummary = scoreBoard.getScoreBoardSummary().getFirst();
        assertEquals(5, gameSummary.getHomeTeamScore());
        assertEquals(2, gameSummary.getAwayTeamScore());
    }

    @Test
    void updateGameScore_OK_badNameFormatting() throws ScoreBoardException, GameException {
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        scoreBoard.initializeGame("GER", "HUN");
        scoreBoard.updateGameScore("  gEr", "hUN   ", new FootballGameScoreUpdateRequest(5, 2));
        FifaWorldCupScoreBoardGameSummary gameSummary = scoreBoard.getScoreBoardSummary().getFirst();
        assertEquals(5, gameSummary.getHomeTeamScore());
        assertEquals(2, gameSummary.getAwayTeamScore());
    }

    // getScoreBoardSummary
    @Test
    void getScoreBoardSummary_OK() throws ScoreBoardException, GameException {
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        scoreBoard.initializeGame("MEXICO", "CANADA");
        scoreBoard.initializeGame("SPAIN", "BRAZIL");
        scoreBoard.initializeGame("GERMANY", "FRANCE");
        scoreBoard.initializeGame("URUGUAY", "ITALY");
        scoreBoard.initializeGame("ARGENTINA", "AUSTRALIA");
        scoreBoard.initializeGame("SLOVENIA", "CROATIA");

        scoreBoard.updateGameScore("MEXICO", "CANADA", new FootballGameScoreUpdateRequest(0, 5));
        scoreBoard.updateGameScore("SPAIN", "BRAZIL", new FootballGameScoreUpdateRequest(10, 2));
        scoreBoard.updateGameScore("GERMANY", "FRANCE", new FootballGameScoreUpdateRequest(2, 2));
        scoreBoard.updateGameScore("URUGUAY", "ITALY", new FootballGameScoreUpdateRequest(6, 6));
        scoreBoard.updateGameScore("SLOVENIA", "CROATIA", new FootballGameScoreUpdateRequest(1, 1));
        scoreBoard.updateGameScore("ARGENTINA", "AUSTRALIA", new FootballGameScoreUpdateRequest(3, 1));

        scoreBoard.finishGame("SLOVENIA", "CROATIA");

        List<FifaWorldCupScoreBoardGameSummary> gameSummaryList = scoreBoard.getScoreBoardSummary();
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
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        List<FifaWorldCupScoreBoardGameSummary> result = scoreBoard.getScoreBoardSummary();
        assertEquals(0, result.size());
    }

    // finishGame

    @ParameterizedTest(name = "Finish Game requests with invalid game names {index}")
    @MethodSource("invalidTeamNamePairsUpdateAndFinish")
    void finishGame_KO(String homeTeamName, String awayTeamName) {
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        assertThrows(GameException.class, () -> scoreBoard.finishGame(homeTeamName, awayTeamName));
    }

    @Test
    void finishGame_OK_existingGame() throws GameException, ScoreBoardException {
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        scoreBoard.initializeGame("MEXICO", "CANADA");
        scoreBoard.updateGameScore("MEXICO", "CANADA", new FootballGameScoreUpdateRequest(0, 5));
        scoreBoard.finishGame("MEXICO", "CANADA");
        assertEquals(0, scoreBoard.getScoreBoardSummary().size());
    }


    @Test
    void finishGame_OK_existingGameBadlyFormatedNames() throws GameException, ScoreBoardException {
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        scoreBoard.initializeGame(" Mexico ", "cAnAda");
        scoreBoard.updateGameScore("mexico", "canada", new FootballGameScoreUpdateRequest(0, 5));
        scoreBoard.finishGame("   MexIco ", "CanaDa   ");
        assertEquals(0, scoreBoard.getScoreBoardSummary().size());
    }

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
