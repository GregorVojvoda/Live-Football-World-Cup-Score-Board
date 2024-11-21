package com.home.gvojvoda.adapter.scoreboard.fifaworldcup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.home.gvojvoda.adapter.game.football.FootballGame;
import com.home.gvojvoda.adapter.game.football.FootballGameScoreUpdateRequest;
import com.home.gvojvoda.adapter.game.football.FootballGameTeamNameUtil;
import com.home.gvojvoda.domain.exception.GameException;
import com.home.gvojvoda.domain.exception.ScoreBoardException;
import com.home.gvojvoda.domain.port.ScoreBoard;

public class FifaWorldCupScoreBoard
        implements ScoreBoard<List<FifaWorldCupScoreBoardGameSummary>, FootballGameScoreUpdateRequest> {

    private Map<String, FootballGame> gameBoard = new HashMap<>();

    @Override
    public void initializeGame(String homeTeamName, String awayTeamName) throws ScoreBoardException, GameException {
        final String formatedHomeTeamName = FootballGameTeamNameUtil.validateAndFormatTeamName(homeTeamName);
        final String formatedAwayTeamName = FootballGameTeamNameUtil.validateAndFormatTeamName(awayTeamName);
        List<String> allTeamNames = new ArrayList<>();
        gameBoard.entrySet()
                .parallelStream()
                .forEach(e -> {
                    allTeamNames.add(e.getValue().getHomeTeamName());
                    allTeamNames.add(e.getValue().getAwayTeamName());
                });
        if (allTeamNames.contains(formatedAwayTeamName) || allTeamNames.contains(formatedHomeTeamName))
            throw new ScoreBoardException("A team cannot be in two active games at the same time");

        gameBoard.put(formatedHomeTeamName + "-" + formatedAwayTeamName,
                new FootballGame(formatedHomeTeamName, formatedAwayTeamName));
    }

    @Override
    public void updateGameScore(String homeTeamName, String awayTeamName,
            FootballGameScoreUpdateRequest updateScoreRequest) throws ScoreBoardException, GameException {
        final String formatedHomeTeamName = FootballGameTeamNameUtil.validateAndFormatTeamName(homeTeamName);
        final String formatedAwayTeamName = FootballGameTeamNameUtil.validateAndFormatTeamName(awayTeamName);
        final String scoreBoardKey = formatedHomeTeamName + "-" + formatedAwayTeamName;
        FootballGame game = Optional.ofNullable(gameBoard.get(scoreBoardKey))
                .orElseThrow(() -> new ScoreBoardException("No game found with the provided theam names"));
        game.updateScore(updateScoreRequest);
    }

    @Override
    public List<FifaWorldCupScoreBoardGameSummary> getScoreBoardSummary() {
        List<FootballGame> allGamesCollection = new ArrayList<>();
        gameBoard.entrySet().stream().forEach(e -> allGamesCollection.add(e.getValue()));

        Comparator<FootballGame> conComparableFootballGame = Comparator
                .comparing(FootballGame::getOverallScore)
                .thenComparing(FootballGame::getMatchStart).reversed();

        List<FifaWorldCupScoreBoardGameSummary> orderedGamesSummaryCollection = new ArrayList<>();
        allGamesCollection.stream().sorted(conComparableFootballGame)
                .forEach(g -> orderedGamesSummaryCollection.add(new FifaWorldCupScoreBoardGameSummary(g)));

        return orderedGamesSummaryCollection;
    }

}
