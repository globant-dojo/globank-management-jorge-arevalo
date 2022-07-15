package com.bank.management.challenge.infrastructure.rest;

import com.bank.management.challenge.domain.models.ReportDto;
import com.bank.management.challenge.domain.ports.services.IReportService;
import com.bank.management.challenge.infrastructure.entities.Movement;
import com.bank.management.challenge.infrastructure.rest.output.FormatOutput;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping("/reports/v1")
@CrossOrigin
@Slf4j
public class ReportController {

    @Autowired
    IReportService reportService;

    @GetMapping("/initialDate/{initialDate}/finalDate/{finalDate}/client/{clientName}")
    @Operation(description = "Movements report by dates and client")
    public ResponseEntity<FormatOutput<List<ReportDto>>> findMovementByDateAndClient(
            @PathVariable("initialDate") String initialDate, @PathVariable("finalDate") String finalDate,
            @PathVariable("clientName") String clientName) {

        return null;
    }

    private ReportDto loadReportData(Movement movement) {

        return null;
    }

}
