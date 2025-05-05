package tn.esprit.tic.se.spr01.TeamandPlayer.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.GameSheet;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.PlayerStatistics;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.TeamStat;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.GameRepository;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.GameSheetRepository;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.PlayerStatisticsRepository;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.TeamStatRepository;

import java.util.List;

@Service
public class MatchPredictionService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameSheetRepository gameSheetRepository;

    @Autowired
    private TeamStatRepository teamStatRepository;

    @Autowired
    private PlayerStatisticsRepository playerStatisticsRepository;


    public PredictionResult predictMatchOutcome(int homeTeamId, int awayTeamId) {
        // 1. Get recent team performance metrics
        TeamPerformanceMetrics homeTeamMetrics = getTeamPerformanceMetrics(homeTeamId);
        TeamPerformanceMetrics awayTeamMetrics = getTeamPerformanceMetrics(awayTeamId);

        // 2. Analyze head-to-head history
        HeadToHeadAnalysis h2hAnalysis = analyzeHeadToHead(homeTeamId, awayTeamId);

        // 3. Calculate form-based factors
        FormFactor homeTeamForm = calculateRecentForm(homeTeamId);
        FormFactor awayTeamForm = calculateRecentForm(awayTeamId);

        // 4. Get player performance impact
        PlayerImpactFactor homePlayerImpact = calculatePlayerImpact(homeTeamId);
        PlayerImpactFactor awayPlayerImpact = calculatePlayerImpact(awayTeamId);

        // 5. Calculate home advantage factor
        double homeAdvantage = 1.2; // 20% advantage for home team

        // 6. Calculate win probabilities and predicted score
        double homeWinProb = calculateWinProbability(
                homeTeamMetrics, awayTeamMetrics,
                h2hAnalysis, homeTeamForm, awayTeamForm,
                homePlayerImpact, awayPlayerImpact,
                homeAdvantage
        );

        double drawProb = calculateDrawProbability(
                homeTeamMetrics, awayTeamMetrics, h2hAnalysis
        );

        double awayWinProb = 1.0 - homeWinProb - drawProb;

        // Predict score
        int[] predictedScore = predictScore(
                homeTeamMetrics, awayTeamMetrics,
                h2hAnalysis, homeTeamForm, awayTeamForm
        );

        return new PredictionResult(
                homeWinProb, drawProb, awayWinProb,
                predictedScore[0], predictedScore[1]
        );
    }

    private TeamPerformanceMetrics getTeamPerformanceMetrics(int teamId) {
        List<TeamStat> recentStats = teamStatRepository.findRecentStatsByTeamId(teamId);


        double avgPerformance = recentStats.stream()
                .mapToDouble(TeamStat::getPerformanceRate)
                .average()
                .orElse(0.0);

        int totalWins = recentStats.isEmpty() ? 0 : recentStats.get(0).getTotalWins();
        int totalLosses = recentStats.isEmpty() ? 0 : recentStats.get(0).getTotalLosses();
        int rankingPoints = recentStats.isEmpty() ? 0 : recentStats.get(0).getRankingPoints();

        return new TeamPerformanceMetrics(
                avgPerformance,
                totalWins,
                totalLosses,
                rankingPoints,
                calculateWinPercentage(totalWins, totalLosses)
        );
    }

    private HeadToHeadAnalysis analyzeHeadToHead(int homeTeamId, int awayTeamId) {
        // Find previous matches between these teams - limit to 5 records
        List<GameSheet> previousMatches = gameSheetRepository.findPreviousMatchesBetweenTeams(
                homeTeamId, awayTeamId);


        int homeWins = 0, awayWins = 0, draws = 0;
        double homeGoalsAvg = 0, awayGoalsAvg = 0;
        double homePossessionAvg = 0, awayPossessionAvg = 0;

        for (GameSheet match : previousMatches) {
            if (match.getHomegoal() > match.getAwaygoal()) {
                if (match.getHomeTeam().getTeamId() == homeTeamId) homeWins++;
                else awayWins++;
            } else if (match.getHomegoal() < match.getAwaygoal()) {
                if (match.getHomeTeam().getTeamId() == awayTeamId) homeWins++;
                else awayWins++;
            } else {
                draws++;
            }

            // Calculate averages for goals and possession
            if (match.getHomeTeam().getTeamId() == homeTeamId) {
                homeGoalsAvg += match.getHomegoal();
                awayGoalsAvg += match.getAwaygoal();
                homePossessionAvg += match.getHomepossessionPercent();
                awayPossessionAvg += match.getAwaypossessionPercent();
            } else {
                homeGoalsAvg += match.getAwaygoal();
                awayGoalsAvg += match.getHomegoal();
                homePossessionAvg += match.getAwaypossessionPercent();
                awayPossessionAvg += match.getHomepossessionPercent();
            }
        }

        int matchCount = previousMatches.size();
        if (matchCount > 0) {
            homeGoalsAvg /= matchCount;
            awayGoalsAvg /= matchCount;
            homePossessionAvg /= matchCount;
            awayPossessionAvg /= matchCount;
        }

        return new HeadToHeadAnalysis(
                homeWins, awayWins, draws,
                homeGoalsAvg, awayGoalsAvg,
                homePossessionAvg, awayPossessionAvg
        );
    }

    private FormFactor calculateRecentForm(int teamId) {
        // Get last 5 games
        List<GameSheet> recentMatches = gameSheetRepository.findRecentMatchesByTeamId(teamId);

        // Use PageRequest to limit to 5 results if needed
        if (recentMatches.size() > 5) {
            recentMatches = recentMatches.subList(0, 5);
        }

        double recentGoalsScored = 0;
        double recentGoalsConceded = 0;
        int wins = 0, losses = 0, draws = 0;

        for (GameSheet match : recentMatches) {
            boolean isHomeTeam = match.getHomeTeam().getTeamId() == teamId;
            int goalsScored = isHomeTeam ? match.getHomegoal() : match.getAwaygoal();
            int goalsConceded = isHomeTeam ? match.getAwaygoal() : match.getHomegoal();

            recentGoalsScored += goalsScored;
            recentGoalsConceded += goalsConceded;

            if (goalsScored > goalsConceded) wins++;
            else if (goalsScored < goalsConceded) losses++;
            else draws++;
        }

        int matchCount = recentMatches.size();
        if (matchCount > 0) {
            recentGoalsScored /= matchCount;
            recentGoalsConceded /= matchCount;
        }

        // Create a form score (weighted sum of recent results)
        double formScore = (wins * 3 + draws * 1) / (double)(Math.max(matchCount, 1) * 3) * 100;

        return new FormFactor(
                formScore,
                recentGoalsScored,
                recentGoalsConceded,
                wins, draws, losses
        );
    }

    private PlayerImpactFactor calculatePlayerImpact(int teamId) {
        // Get top performing players from the team
        List<PlayerStatistics> topPlayerStats = playerStatisticsRepository.findTopPerformingPlayersByTeamId(teamId);

        if (topPlayerStats.size() > 5) {
            topPlayerStats = topPlayerStats.subList(0, 5);
        }

        double avgPerformance = topPlayerStats.stream()
                .mapToDouble(PlayerStatistics::getPerformanceRate)
                .average()
                .orElse(0.0);

        double goalScoringRate = topPlayerStats.stream()
                .mapToDouble(PlayerStatistics::getGoalsScored)
                .sum() / Math.max(topPlayerStats.size(), 1); // Average goals per player

        double assistRate = topPlayerStats.stream()
                .mapToDouble(PlayerStatistics::getAssists)
                .sum() / Math.max(topPlayerStats.size(), 1); // Average assists per player

        return new PlayerImpactFactor(avgPerformance, goalScoringRate, assistRate);
    }

    private double calculateWinProbability(
            TeamPerformanceMetrics homeMetrics, TeamPerformanceMetrics awayMetrics,
            HeadToHeadAnalysis h2h, FormFactor homeForm, FormFactor awayForm,
            PlayerImpactFactor homePlayerImpact, PlayerImpactFactor awayPlayerImpact,
            double homeAdvantage
    ) {
        // Define weights for different factors
        final double TEAM_PERFORMANCE_WEIGHT = 0.3;
        final double HEAD_TO_HEAD_WEIGHT = 0.2;
        final double FORM_WEIGHT = 0.3;
        final double PLAYER_IMPACT_WEIGHT = 0.2;

        // Team performance score
        double homeTeamPerformanceScore =
                homeMetrics.getWinPercentage() * 0.4 +
                        homeMetrics.getPerformanceRate() * 0.6;

        double awayTeamPerformanceScore =
                awayMetrics.getWinPercentage() * 0.4 +
                        awayMetrics.getPerformanceRate() * 0.6;

        // Prevent division by zero
        double totalTeamPerformance = homeTeamPerformanceScore + awayTeamPerformanceScore;
        double teamPerformanceFactor = totalTeamPerformance > 0 ?
                homeTeamPerformanceScore / totalTeamPerformance : 0.5;

        // Head to head factor
        double h2hMatchCount = h2h.getHomeWins() + h2h.getAwayWins() + h2h.getDraws();
        double h2hFactor = 0.5; // default to even odds

        if (h2hMatchCount > 0) {
            h2hFactor = (h2h.getHomeWins() + 0.5 * h2h.getDraws()) / h2hMatchCount;
        }

        // Form factor
        double homeFormScore = Math.max(homeForm.getFormScore(), 0.1); // Prevent zero
        double awayFormScore = Math.max(awayForm.getFormScore(), 0.1); // Prevent zero
        double formFactor = homeFormScore / (homeFormScore + awayFormScore);

        // Player impact factor
        double homePlayerScore =
                homePlayerImpact.getAvgPerformance() * 0.5 +
                        homePlayerImpact.getGoalScoringRate() * 0.3 +
                        homePlayerImpact.getAssistRate() * 0.2;

        double awayPlayerScore =
                awayPlayerImpact.getAvgPerformance() * 0.5 +
                        awayPlayerImpact.getGoalScoringRate() * 0.3 +
                        awayPlayerImpact.getAssistRate() * 0.2;

        // Prevent division by zero
        double totalPlayerScore = homePlayerScore + awayPlayerScore;
        double playerImpactFactor = totalPlayerScore > 0 ?
                homePlayerScore / totalPlayerScore : 0.5;

        // Calculate weighted probability
        double rawProb =
                TEAM_PERFORMANCE_WEIGHT * teamPerformanceFactor +
                        HEAD_TO_HEAD_WEIGHT * h2hFactor +
                        FORM_WEIGHT * formFactor +
                        PLAYER_IMPACT_WEIGHT * playerImpactFactor;

        // Apply home advantage
        return Math.min(rawProb * homeAdvantage, 0.95); // Cap at 95% max probability
    }

    private double calculateDrawProbability(
            TeamPerformanceMetrics homeMetrics, TeamPerformanceMetrics awayMetrics,
            HeadToHeadAnalysis h2h
    ) {
        // Calculate how closely matched the teams are
        double performanceDiff = Math.abs(
                homeMetrics.getPerformanceRate() - awayMetrics.getPerformanceRate()
        );

        double rankingDiff = Math.abs(
                homeMetrics.getRankingPoints() - awayMetrics.getRankingPoints()
        );

        // Normalize differences
        double normalizedPerfDiff = Math.min(performanceDiff / 50.0, 1.0);
        double normalizedRankDiff = Math.min(rankingDiff / 1000.0, 1.0);

        // Teams with similar strength have higher draw probability
        double strengthSimilarity = 1.0 - (normalizedPerfDiff * 0.5 + normalizedRankDiff * 0.5);

        // Historical draw tendency
        double h2hMatchCount = h2h.getHomeWins() + h2h.getAwayWins() + h2h.getDraws();
        double historicalDrawRate = h2hMatchCount > 0 ? h2h.getDraws() / h2hMatchCount : 0.25;

        // Combine factors - cap at reasonable rates
        return Math.min(0.25 * strengthSimilarity + 0.15 * historicalDrawRate, 0.5);
    }

    private int[] predictScore(
            TeamPerformanceMetrics homeMetrics, TeamPerformanceMetrics awayMetrics,
            HeadToHeadAnalysis h2h, FormFactor homeForm, FormFactor awayForm
    ) {
        // Predict scores based on average goals scored/conceded and head-to-head history
        double predictedHomeGoals =
                (homeForm.getRecentGoalsScored() * 0.4) +
                        (awayForm.getRecentGoalsConceded() * 0.4) +
                        (h2h.getHomeGoalsAvg() * 0.2);

        double predictedAwayGoals =
                (awayForm.getRecentGoalsScored() * 0.4) +
                        (homeForm.getRecentGoalsConceded() * 0.4) +
                        (h2h.getAwayGoalsAvg() * 0.2);

        // Apply home advantage factor to goals
        predictedHomeGoals *= 1.1;

        // Round to integers
        return new int[] {
                (int) Math.round(predictedHomeGoals),
                (int) Math.round(predictedAwayGoals)
        };
    }

    // Helper method to calculate win percentage
    private double calculateWinPercentage(int wins, int losses) {
        int totalGames = wins + losses;
        return totalGames > 0 ? (double) wins / totalGames : 0.0;
    }

    // Inner classes for structured data

    public static class PredictionResult {
        private double homeWinProbability;
        private double drawProbability;
        private double awayWinProbability;
        private int predictedHomeGoals;
        private int predictedAwayGoals;

        public PredictionResult(double homeWinProbability, double drawProbability,
                                double awayWinProbability, int predictedHomeGoals,
                                int predictedAwayGoals) {
            this.homeWinProbability = homeWinProbability;
            this.drawProbability = drawProbability;
            this.awayWinProbability = awayWinProbability;
            this.predictedHomeGoals = predictedHomeGoals;
            this.predictedAwayGoals = predictedAwayGoals;
        }

        public double getHomeWinProbability() { return homeWinProbability; }
        public double getDrawProbability() { return drawProbability; }
        public double getAwayWinProbability() { return awayWinProbability; }
        public int getPredictedHomeGoals() { return predictedHomeGoals; }
        public int getPredictedAwayGoals() { return predictedAwayGoals; }

        @Override
        public String toString() {
            return String.format(
                    "Home win: %.1f%%, Draw: %.1f%%, Away win: %.1f%%, Predicted score: %d-%d",
                    homeWinProbability * 100,
                    drawProbability * 100,
                    awayWinProbability * 100,
                    predictedHomeGoals,
                    predictedAwayGoals
            );
        }
    }

    public static class TeamPerformanceMetrics {
        private double performanceRate;
        private int totalWins;
        private int totalLosses;
        private int rankingPoints;
        private double winPercentage;

        public TeamPerformanceMetrics(double performanceRate, int totalWins, int totalLosses,
                                      int rankingPoints, double winPercentage) {
            this.performanceRate = performanceRate;
            this.totalWins = totalWins;
            this.totalLosses = totalLosses;
            this.rankingPoints = rankingPoints;
            this.winPercentage = winPercentage;
        }

        public double getPerformanceRate() { return performanceRate; }
        public int getTotalWins() { return totalWins; }
        public int getTotalLosses() { return totalLosses; }
        public int getRankingPoints() { return rankingPoints; }
        public double getWinPercentage() { return winPercentage; }
    }

    public static class HeadToHeadAnalysis {
        private int homeWins;
        private int awayWins;
        private int draws;
        private double homeGoalsAvg;
        private double awayGoalsAvg;
        private double homePossessionAvg;
        private double awayPossessionAvg;

        public HeadToHeadAnalysis(int homeWins, int awayWins, int draws,
                                  double homeGoalsAvg, double awayGoalsAvg,
                                  double homePossessionAvg, double awayPossessionAvg) {
            this.homeWins = homeWins;
            this.awayWins = awayWins;
            this.draws = draws;
            this.homeGoalsAvg = homeGoalsAvg;
            this.awayGoalsAvg = awayGoalsAvg;
            this.homePossessionAvg = homePossessionAvg;
            this.awayPossessionAvg = awayPossessionAvg;
        }

        public int getHomeWins() { return homeWins; }
        public int getAwayWins() { return awayWins; }
        public int getDraws() { return draws; }
        public double getHomeGoalsAvg() { return homeGoalsAvg; }
        public double getAwayGoalsAvg() { return awayGoalsAvg; }
        public double getHomePossessionAvg() { return homePossessionAvg; }
        public double getAwayPossessionAvg() { return awayPossessionAvg; }
    }

    public static class FormFactor {
        private double formScore;
        private double recentGoalsScored;
        private double recentGoalsConceded;
        private int recentWins;
        private int recentDraws;
        private int recentLosses;

        public FormFactor(double formScore, double recentGoalsScored, double recentGoalsConceded,
                          int recentWins, int recentDraws, int recentLosses) {
            this.formScore = formScore;
            this.recentGoalsScored = recentGoalsScored;
            this.recentGoalsConceded = recentGoalsConceded;
            this.recentWins = recentWins;
            this.recentDraws = recentDraws;
            this.recentLosses = recentLosses;
        }

        public double getFormScore() { return formScore; }
        public double getRecentGoalsScored() { return recentGoalsScored; }
        public double getRecentGoalsConceded() { return recentGoalsConceded; }
        public int getRecentWins() { return recentWins; }
        public int getRecentDraws() { return recentDraws; }
        public int getRecentLosses() { return recentLosses; }
    }

    public static class PlayerImpactFactor {
        private double avgPerformance;
        private double goalScoringRate;
        private double assistRate;

        public PlayerImpactFactor(double avgPerformance, double goalScoringRate, double assistRate) {
            this.avgPerformance = avgPerformance;
            this.goalScoringRate = goalScoringRate;
            this.assistRate = assistRate;
        }

        public double getAvgPerformance() { return avgPerformance; }
        public double getGoalScoringRate() { return goalScoringRate; }
        public double getAssistRate() { return assistRate; }
    }
}
