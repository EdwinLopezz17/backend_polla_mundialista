package com.polla.demo.controllers;

import com.polla.demo.models.Match;
import com.polla.demo.repositories.MatchRepository;
import com.polla.demo.services.MatchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "*")
public class MatchController {

    private final MatchRepository matchRepository;
    private final MatchService matchService;

    public MatchController(
            MatchRepository matchRepository,
            MatchService matchService) {
        this.matchRepository = matchRepository;
        this.matchService = matchService;
    }

    @GetMapping
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @PostMapping
    public Match createMatch(@RequestBody Match match) {
        return matchRepository.save(match);
    }

    @PostMapping("/{id}/finish")
    public Match finishMatch(
            @PathVariable Long id,
            @RequestBody Match actualResults) {
        return matchService.finishMatch(id, actualResults);
    }

}

