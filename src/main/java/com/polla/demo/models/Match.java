package com.polla.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "home_team", nullable = false)
    private String homeTeam;

    @Column(name = "away_team", nullable = false)
    private String awayTeam;

    @Column(name = "match_date", nullable = false)
    private LocalDateTime matchDate;

    @Column(name = "full_time_result")
    private String fullTimeResult; // "1", "2", or "X"

    @Column(name = "qualified_team")
    private String qualifiedTeam; // "1" or "2" (Knockout stage)

    @Column(name = "home_goals")
    private Integer homeGoals;

    @Column(name = "away_goals")
    private Integer awayGoals;

    @Column(name = "penalty_awarded")
    private Boolean penaltyAwarded;

    @Column(name = "yellow_card")
    private Boolean yellowCard;

    @Column(name = "red_card")
    private Boolean redCard;
}
