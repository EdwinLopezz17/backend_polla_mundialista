package com.polla.demo.controllers;

import com.polla.demo.models.dtos.ScoreMatrixDTO;
import com.polla.demo.services.ReportService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/score-matrix")
    public ScoreMatrixDTO getScoreMatrix() {
        return reportService.getScoreMatrix();
    }
}

