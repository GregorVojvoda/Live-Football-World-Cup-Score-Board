package com.home.gvojvoda.adapter.scoreboard.fifaworldcup;

import com.home.gvojvoda.adapter.game.football.FootballGame;
import com.home.gvojvoda.adapter.game.football.FootballGameScore;
import com.home.gvojvoda.domain.exception.GameException;
import com.home.gvojvoda.domain.exception.ScoreBoardException;
import com.home.gvojvoda.domain.port.ScoreBoard;
import com.home.gvojvoda.domain.util.GameTeamNameUtil;

import java.util.*;

public class FifaWorldCupScoreBoard
        implements ScoreBoard<List<FifaWorldCupScoreBoardGameSummary>, FootballGameScore> {

    private final Map<String, FootballGame> scoreBoard = new HashMap<>();

    @Override
    public void initializeGame(String homeTeamName, String awayTeamName) throws ScoreBoardException, GameException {
        final String formatedHomeTeamName = GameTeamNameUtil.validateAndFormatTeamName(homeTeamName);
        final String formatedAwayTeamName = GameTeamNameUtil.validateAndFormatTeamName(awayTeamName);

        List<String> allTeamNames = extractTeamNamesInTheScoreBoard();

        if (allTeamNames.contains(formatedAwayTeamName) || allTeamNames.contains(formatedHomeTeamName))
            throw new ScoreBoardException("A team cannot be in two active games at the same time");

        scoreBoard.put(scoreBoardKeyGenerator(formatedHomeTeamName, formatedAwayTeamName),
                new FootballGame(formatedHomeTeamName, formatedAwayTeamName));
    }

    @Override
    public void updateGameScore(String homeTeamName, String awayTeamName,
                                FootballGameScore score) throws ScoreBoardException, GameException {
        final String formatedHomeTeamName = GameTeamNameUtil.validateAndFormatTeamName(homeTeamName);
        final String formatedAwayTeamName = GameTeamNameUtil.validateAndFormatTeamName(awayTeamName);
        final String scoreBoardKey = scoreBoardKeyGenerator(formatedHomeTeamName, formatedAwayTeamName);

        FootballGame game = Optional.ofNullable(scoreBoard.get(scoreBoardKey))
                .orElseThrow(() -> new ScoreBoardException("No game found with the provided team names"));
        game.updateScore(score);
    }

    @Override
    public List<FifaWorldCupScoreBoardGameSummary> getScoreBoardSummary() {
        List<FootballGame> allGamesCollection = new ArrayList<>();
        scoreBoard.forEach((key, value) -> allGamesCollection.add(value));

        Comparator<FootballGame> conComparableFootballGame = Comparator
                .comparing(FootballGame::getOverallScore)
                .thenComparing(FootballGame::getMatchStart).reversed();

        List<FifaWorldCupScoreBoardGameSummary> orderedGamesSummaryCollection = new ArrayList<>();
        allGamesCollection.stream().sorted(conComparableFootballGame)
                .forEach(g -> orderedGamesSummaryCollection.add(new FifaWorldCupScoreBoardGameSummary(g)));

        return orderedGamesSummaryCollection;
    }

    @Override
    public void endGame(String homeTeamName, String awayTeamName) throws GameException, ScoreBoardException {
        final String formatedHomeTeamName = GameTeamNameUtil.validateAndFormatTeamName(homeTeamName);
        final String formatedAwayTeamName = GameTeamNameUtil.validateAndFormatTeamName(awayTeamName);

        final String key = scoreBoardKeyGenerator(formatedHomeTeamName, formatedAwayTeamName);
        FootballGame footballGame = Optional.ofNullable(scoreBoard.get(key)).orElseThrow(() -> new ScoreBoardException("No game found with the provided team names"));
        footballGame.endGame();

        scoreBoard.remove(key);
    }

    private String scoreBoardKeyGenerator(String formatedHomeTeamName, String formatedAwayTeamName) {
        return formatedHomeTeamName + "-" + formatedAwayTeamName;
    }

    private List<String> extractTeamNamesInTheScoreBoard() {
        List<String> allTeamNames = new ArrayList<>();
        scoreBoard.entrySet()
                .parallelStream()
                .forEach(e -> {
                    allTeamNames.add(e.getValue().getHomeTeamName());
                    allTeamNames.add(e.getValue().getAwayTeamName());
                });
        return allTeamNames;
    }
}
