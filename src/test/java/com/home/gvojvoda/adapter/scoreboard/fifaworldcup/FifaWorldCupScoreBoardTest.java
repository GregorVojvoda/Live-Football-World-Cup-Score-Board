package com.home.gvojvoda.adapter.scoreboard.fifaworldcup;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.home.gvojvoda.domain.exception.GameException;
import com.home.gvojvoda.domain.exception.ScoreBoardException;

class FifaWorldCupScoreBoardTest {

    // initializeGame
    @ParameterizedTest(name = "Invalid team names {index}")
    @MethodSource("invalidTeamNamePairs")
    void initializeGame_KO_invalidGameNames(String homeTeamName, String awayTeamName) throws ScoreBoardException, GameException {
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        assertThrows(GameException.class, () ->  scoreBoard.initializeGame(homeTeamName, awayTeamName));
    }

    @ParameterizedTest(name = "Team Names already exist {index}")
    @MethodSource("validTeamNamePaisSloItaHun")
    void initializationgame_KO_teamsAlreadyInGame(String homeTeamName, String awayTeamName) throws ScoreBoardException, GameException{
        FifaWorldCupScoreBoard scoreBoard = new FifaWorldCupScoreBoard();
        scoreBoard.initializeGame("ITA", "SLO");
        assertThrows(ScoreBoardException.class, () -> scoreBoard.initializeGame(homeTeamName, awayTeamName));
    }

    private static Stream<Arguments> validTeamNamePaisSloItaHun() {
        return Stream.of(
            Arguments.of("   SLO   ", "   ITA  "),
            Arguments.of("SLO", "ITA"),
            Arguments.of("SLO", "HUN"),
            Arguments.of("ITA", "SLO"),
            Arguments.of("ITA", "HUN"),
            Arguments.of("HUN", "SLO"),
            Arguments.of("HUN", "ITA")
        );
    }

    private static Stream<Arguments> invalidTeamNamePairs() {
        return Stream.of(
            Arguments.of(null, "Slovenia"),
            Arguments.of("Slovenia", null),
            Arguments.of(null, null),
            Arguments.of( "   ", "         "),
            Arguments.of("Slo%Venia", "Italia"),
            Arguments.of("Slovenia", "Ital;i"),
            Arguments.of("SLO", "SLO"),
            Arguments.of("SLO", "   SLO    ")
        );
    }



}
