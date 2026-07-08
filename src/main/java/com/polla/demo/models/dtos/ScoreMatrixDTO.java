package com.polla.demo.models.dtos;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ScoreMatrixDTO {

    private List<MatchSummary> matches;
    private List<UserRow> matrix;

    @Data
    public static class MatchSummary {
        private Long id;
        private String opponents;
    }

    @Data
    public static class UserRow {
        private String userName;
        private Integer totalScore;
        private Map<Long, Integer> scoresByMatch;
    }
}
