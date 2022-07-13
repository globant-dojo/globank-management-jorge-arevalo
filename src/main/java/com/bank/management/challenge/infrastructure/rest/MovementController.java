package com.bank.management.challenge.infrastructure.rest;

import com.bank.management.challenge.domain.models.MovementDto;
import com.bank.management.challenge.domain.ports.services.IMovementService;
import com.bank.management.challenge.infrastructure.config.exception.GeneralValidationException;
import com.bank.management.challenge.infrastructure.rest.input.MovementInput;
import com.bank.management.challenge.infrastructure.rest.input.FormatInput;
import com.bank.management.challenge.infrastructure.rest.output.FormatMessage;
import com.bank.management.challenge.infrastructure.rest.output.FormatOutput;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/movements/v1")
@CrossOrigin
@Slf4j
public class MovementController {

    @Autowired
    private IMovementService movementService;

    @GetMapping
    @Operation(description = "List of all movements")
    public ResponseEntity<FormatOutput<List<MovementDto>>> findAll() {
        log.info("Getting the movements list");
        HttpStatus status = HttpStatus.OK;
        List<MovementDto> movementDtoList = null;
        try {
            movementDtoList = movementService.findAll();
            if(movementDtoList.isEmpty()) {
                status = HttpStatus.NOT_FOUND;
            }
        } catch (IllegalArgumentException iex) {
            log.error(iex.getMessage(), iex);
            status = HttpStatus.BAD_REQUEST;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        FormatOutput<List<MovementDto>> output = new FormatOutput<>();
        output.setData(movementDtoList);
        output.setMessages(List.of(new FormatMessage(String.valueOf(status.value()), status.getReasonPhrase())));
        return new ResponseEntity<>(output, status);
    }

    @GetMapping("/{id}")
    @Operation(description = "Get movement data by id")
    public ResponseEntity<FormatOutput<MovementDto>> findById(@PathVariable("id") String id) {
        validateId(id);
        log.info("Getting movement with Id # {}", id);
        HttpStatus status = HttpStatus.OK;
        MovementDto movementDto = null;
        try {
            movementDto = movementService.findById(id);
            if(movementDto == null) {
                status = HttpStatus.NOT_FOUND;
            }
        } catch (IllegalArgumentException iex) {
            log.error(iex.getMessage(), iex);
            status = HttpStatus.BAD_REQUEST;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        FormatOutput<MovementDto> output = new FormatOutput<>();
        output.setData(movementDto);
        output.setMessages(List.of(new FormatMessage(String.valueOf(status.value()), status.getReasonPhrase())));
        return new ResponseEntity<>(output, status);
    }

    @PostMapping
    @Operation(description = "Save movement data")
    public ResponseEntity<FormatOutput<MovementDto>> save(
            @Valid @RequestBody FormatInput<MovementInput> movementInput) {
        HttpStatus status = HttpStatus.CREATED;
        MovementDto movementDto = movementService.save(movementInput.getData());
        log.info("Saving movement with Id # {}", movementDto.getId());
        FormatMessage message = new FormatMessage(String.valueOf(status.value()), status.getReasonPhrase());
        FormatOutput<MovementDto> formatOutput = new FormatOutput<>(movementDto, List.of(message));
        return new ResponseEntity<>(formatOutput, status);
    }

    @PutMapping("/{id}")
    @Operation(description = "Update movement data using id")
    public ResponseEntity<FormatOutput<MovementDto>> update(
            @PathVariable("id") @NotNull String id,
            @Valid @RequestBody FormatInput<MovementInput> movementInput) {
        validateId(id);
        log.info("Updating client with Id # {}", id);
        HttpStatus status = HttpStatus.OK;
        MovementDto movementDto = movementService.update(id, movementInput.getData());
        FormatMessage message = new FormatMessage(String.valueOf(status.value()), status.getReasonPhrase());
        FormatOutput<MovementDto> formatOutput = new FormatOutput<>(movementDto, List.of(message));
        return new ResponseEntity<>(formatOutput, status);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Delete movement data using id")
    public ResponseEntity<FormatOutput<MovementDto>> delete(@PathVariable("id") String id) {
        validateId(id);
        log.info("Deleting movement with Id # {}", id);
        HttpStatus status = HttpStatus.OK;
        movementService.delete(id);
        FormatMessage message = new FormatMessage(String.valueOf(status.value()), status.getReasonPhrase());
        FormatOutput<MovementDto> formatOutput = new FormatOutput<>(null, List.of(message));
        return new ResponseEntity<>(formatOutput, status);
    }

    private void validateId(String id) {
        try {
            log.info("Validating movement Id # {}", UUID.fromString(id));
        } catch (IllegalArgumentException iex) {
            FormatMessage message = new FormatMessage("data.id", "Not valid id");
            throw new GeneralValidationException(List.of(message));
        }
    }

}
