package com.polla.demo.services;

import com.polla.demo.models.Bet;
import com.polla.demo.models.Match;
import com.polla.demo.repositories.BetRepository;
import com.polla.demo.repositories.MatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final BetRepository betRepository;

    public MatchService(MatchRepository matchRepository, BetRepository betRepository) {
        this.matchRepository = matchRepository;
        this.betRepository = betRepository;
    }

    @Transactional
    public Match finishMatch(Long matchId, Match actualResults) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        match.setFullTimeResult(actualResults.getFullTimeResult());
        match.setQualifiedTeam(actualResults.getQualifiedTeam());
        match.setHomeGoals(actualResults.getHomeGoals());
        match.setAwayGoals(actualResults.getAwayGoals());
        match.setPenaltyAwarded(actualResults.getPenaltyAwarded());
        match.setYellowCard(actualResults.getYellowCard());
        match.setRedCard(actualResults.getRedCard());

        matchRepository.save(match);

        List<Bet> bets = betRepository.findByMatchId(matchId);
        for (Bet bet : bets) {
            int points = calculatePoints(bet, match);
            bet.setPointsEarned(points);
            betRepository.save(bet);
        }

        return match;
    }

    private int calculatePoints(Bet bet, Match actualMatch) {
        int points = 0;

        if (bet.getFullTimePrediction().equalsIgnoreCase(actualMatch.getFullTimeResult())) {
            if (actualMatch.getFullTimeResult().equalsIgnoreCase("X")) {
                points += 2;
            } else {
                points += 4;
            }
        }

        if (actualMatch.getFullTimeResult().equalsIgnoreCase("X") &&
                bet.getQualifiedTeamPrediction().equalsIgnoreCase(actualMatch.getQualifiedTeam())) {
            points += 1;
        }

        if (bet.getHomeGoalsPrediction().equals(actualMatch.getHomeGoals())
                && bet.getAwayGoalsPrediction().equals(actualMatch.getAwayGoals())) {
            points += 3;
        }

        if (bet.getPenaltyPrediction().equals(actualMatch.getPenaltyAwarded())) {
            points += 1;
        }
        if (bet.getYellowCardPrediction().equals(actualMatch.getYellowCard())) {
            points += 1;
        }
        if (bet.getRedCardPrediction().equals(actualMatch.getRedCard())) {
            points += 1;
        }

        return points;
    }
}