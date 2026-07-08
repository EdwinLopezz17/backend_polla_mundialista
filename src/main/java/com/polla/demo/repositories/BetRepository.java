package com.polla.demo.repositories;

import com.polla.demo.models.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BetRepository extends JpaRepository<Bet, Long> {
    List<Bet> findByMatchId(Long matchId);
    List<Bet> findByUserId(Long userId);
    Optional<Bet> findByUserIdAndMatchId(Long userId, Long matchId);
}
