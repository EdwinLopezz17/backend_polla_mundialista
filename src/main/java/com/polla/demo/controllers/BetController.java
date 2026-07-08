package com.polla.demo.controllers;

import com.polla.demo.models.Bet;
import com.polla.demo.models.Match;
import com.polla.demo.repositories.BetRepository;
import com.polla.demo.repositories.MatchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bets")
@CrossOrigin(origins = "*")
public class BetController {

    private final BetRepository betRepository;
    private final MatchRepository matchRepository;

    public BetController(BetRepository betRepository, MatchRepository matchRepository) {
        this.betRepository = betRepository;
        this.matchRepository = matchRepository;
    }

    @PostMapping
    public ResponseEntity<?> createOrUpdateBet(@RequestBody Bet newBet) {
        if (newBet.getUser() == null || newBet.getMatch() == null) {
            return ResponseEntity.badRequest().body("User or Match info missing");
        }

        Match match = matchRepository.findById(newBet.getMatch().getId())
                .orElseThrow(() -> new RuntimeException("Match not found"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime matchStart = match.getMatchDate();

        long minutesUntilMatch = ChronoUnit.MINUTES.between(now, matchStart);

        if (minutesUntilMatch < 15) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Auestas cerradas. Faltan menos de 15 minutos o el partido ya empezó.");
        }

        Optional<Bet> existingBet = betRepository.findByUserIdAndMatchId(
                newBet.getUser().getId(),
                newBet.getMatch().getId()
        );

        if (existingBet.isPresent()) {
            Bet betToUpdate = existingBet.get();
            betToUpdate.setFullTimePrediction(newBet.getFullTimePrediction());
            betToUpdate.setQualifiedTeamPrediction(newBet.getQualifiedTeamPrediction());
            betToUpdate.setHomeGoalsPrediction(newBet.getHomeGoalsPrediction());
            betToUpdate.setAwayGoalsPrediction(newBet.getAwayGoalsPrediction());
            betToUpdate.setPenaltyPrediction(newBet.getPenaltyPrediction());
            betToUpdate.setYellowCardPrediction(newBet.getYellowCardPrediction());
            betToUpdate.setRedCardPrediction(newBet.getRedCardPrediction());

            Bet savedBet = betRepository.save(betToUpdate);
            return ResponseEntity.ok(savedBet);
        }

        newBet.setPointsEarned(0);
        Bet savedBet = betRepository.save(newBet);
        return ResponseEntity.ok(savedBet);
    }

    @GetMapping
    public List<Bet> getAllBets() {
        return betRepository.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<Bet> getBetsByUserId(@PathVariable Long userId) {
        return betRepository.findByUserId(userId);
    }

    @GetMapping("/match/{matchId}")
    public List<Bet> getBetsByMatchId(@PathVariable Long matchId) {
        return betRepository.findByMatchId(matchId);
    }
}