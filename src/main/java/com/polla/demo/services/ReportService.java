package com.polla.demo.services;

import com.polla.demo.models.Bet;
import com.polla.demo.models.Match;
import com.polla.demo.models.User;
import com.polla.demo.models.dtos.ScoreMatrixDTO;
import com.polla.demo.repositories.BetRepository;
import com.polla.demo.repositories.MatchRepository;
import com.polla.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final BetRepository betRepository;

    public ReportService(UserRepository userRepository, MatchRepository matchRepository, BetRepository betRepository) {
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
        this.betRepository = betRepository;
    }

    public ScoreMatrixDTO getScoreMatrix() {
        ScoreMatrixDTO dto = new ScoreMatrixDTO();

        List<Match> matches = matchRepository.findAll();
        List<ScoreMatrixDTO.MatchSummary> matchSummaries = new ArrayList<>();
        for (Match match : matches) {
            ScoreMatrixDTO.MatchSummary summary = new ScoreMatrixDTO.MatchSummary();
            summary.setId(match.getId());
            summary.setOpponents(match.getHomeTeam() + " vs " + match.getAwayTeam());
            matchSummaries.add(summary);
        }
        dto.setMatches(matchSummaries);

        List<User> users = userRepository.findAll();
        List<ScoreMatrixDTO.UserRow> rows = new ArrayList<>();

        for (User user : users) {
            ScoreMatrixDTO.UserRow row = new ScoreMatrixDTO.UserRow();
            row.setUserName(user.getFullName());

            int dynamicTotalScore = user.getBaseScore();

            List<Bet> userBets = betRepository.findByUserId(user.getId());
            Map<Long, Integer> scoresByMatch = new HashMap<>();

            for (Bet bet : userBets) {
                scoresByMatch.put(bet.getMatch().getId(), bet.getPointsEarned());
                dynamicTotalScore += bet.getPointsEarned();
            }

            row.setTotalScore(dynamicTotalScore);
            row.setScoresByMatch(scoresByMatch);
            rows.add(row);
        }

        dto.setMatrix(rows);
        return dto;
    }
}