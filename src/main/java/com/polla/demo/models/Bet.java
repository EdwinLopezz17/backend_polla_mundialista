package com.polla.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(name = "full_time_prediction", nullable = false)
    private String fullTimePrediction; // "1", "2", or "X"

    @Column(name = "qualified_team_prediction", nullable = false)
    private String qualifiedTeamPrediction; // "1" or "2"

    @Column(name = "home_goals_prediction", nullable = false)
    private Integer homeGoalsPrediction;

    @Column(name = "away_goals_prediction", nullable = false)
    private Integer awayGoalsPrediction;

    @Column(name = "penalty_prediction")
    private Boolean penaltyPrediction;

    @Column(name = "yellow_card_prediction")
    private Boolean yellowCardPrediction;

    @Column(name = "red_card_prediction")
    private Boolean redCardPrediction;

    @Column(name = "points_earned", nullable = false)
    private Integer pointsEarned = 0;
}
