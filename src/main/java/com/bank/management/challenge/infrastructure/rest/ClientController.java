package com.bank.management.challenge.infrastructure.rest;

import com.bank.management.challenge.domain.models.ClientDto;
import com.bank.management.challenge.domain.ports.services.IClientService;
import com.bank.management.challenge.infrastructure.config.exception.GeneralValidationException;
import com.bank.management.challenge.infrastructure.rest.input.ClientInput;
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
@RequestMapping("/clients/v1")
@CrossOrigin
@Slf4j
public class ClientController {

    @Autowired
    IClientService clientService;

    @GetMapping
    @Operation(description = "List of all clients")
    public ResponseEntity<FormatOutput<List<ClientDto>>> findAll() {
        log.info("Getting the clients list");
        HttpStatus status = HttpStatus.OK;
        List<ClientDto> clientDtoList = null;
        try {
            clientDtoList = clientService.findAll();
            if(clientDtoList.isEmpty()) {
                status = HttpStatus.NOT_FOUND;
            }
        } catch (IllegalArgumentException iex) {
            log.error(iex.getMessage(), iex);
            status = HttpStatus.BAD_REQUEST;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        FormatOutput<List<ClientDto>> output = new FormatOutput<>();
        output.setData(clientDtoList);
        output.setMessages(List.of(new FormatMessage(String.valueOf(status.value()), status.getReasonPhrase())));
        return new ResponseEntity<>(output, status);
    }

    @GetMapping("/{id}")
    @Operation(description = "Get client data by id")
    public ResponseEntity<FormatOutput<ClientDto>> findById(@PathVariable("id") String id) {
        validateId(id);
        log.info("Getting client with Id # {}", id);
        HttpStatus status = HttpStatus.OK;
        ClientDto clientDto = null;
        try {
            clientDto = clientService.findById(id);
            if(clientDto == null) {
                status = HttpStatus.NOT_FOUND;
            }
        } catch (IllegalArgumentException iex) {
            log.error(iex.getMessage(), iex);
            status = HttpStatus.BAD_REQUEST;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        FormatOutput<ClientDto> output =new FormatOutput<>();
        output.setData(clientDto);
        output.setMessages(List.of(new FormatMessage(String.valueOf(status.value()), status.getReasonPhrase())));
        return new ResponseEntity<>(output, status);
    }

    @PostMapping
    @Operation(description = "Save client data")
    public ResponseEntity<FormatOutput<ClientDto>> save(
            @Valid @RequestBody FormatInput<ClientInput> clientInput) {
        HttpStatus status = HttpStatus.CREATED;
        ClientDto clientDto = clientService.save(clientInput.getData());
        log.info("Saving client with Id # {}", clientDto.getId());
        FormatMessage message = new FormatMessage(String.valueOf(status.value()), status.getReasonPhrase());
        FormatOutput<ClientDto> formatOutput = new FormatOutput<>(clientDto, List.of(message));
        return new ResponseEntity<>(formatOutput, status);
    }

    @PutMapping("/{id}")
    @Operation(description = "Update client data using id")
    public ResponseEntity<FormatOutput<ClientDto>> update(
            @PathVariable("id") @NotNull String id,
            @Valid @RequestBody FormatInput<ClientInput> clientInput) {
        validateId(id);
        log.info("Updating client with Id # {}", id);
        HttpStatus status = HttpStatus.OK;
        ClientDto clientDto = clientService.update(id, clientInput.getData());
        FormatMessage message = new FormatMessage(String.valueOf(status.value()), status.getReasonPhrase());
        FormatOutput<ClientDto> formatOutput = new FormatOutput<>(clientDto, List.of(message));
        return new ResponseEntity<>(formatOutput, status);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Delete client data using id")
    public ResponseEntity<FormatOutput<ClientDto>> delete(@PathVariable("id") String id) {
        validateId(id);
        log.info("Deleting client with Id # {}", id);
        HttpStatus status = HttpStatus.OK;
        clientService.delete(id);
        FormatMessage message = new FormatMessage(String.valueOf(status.value()), status.getReasonPhrase());
        FormatOutput<ClientDto> formatOutput = new FormatOutput<>(ClientDto.builder().build(), List.of(message));
        return new ResponseEntity<>(formatOutput, status);
    }

    private void validateId(String id) {
        try {
            log.info("Validating client Id # {}", UUID.fromString(id));
        } catch (IllegalArgumentException iex) {
            FormatMessage message = new FormatMessage("data.id", "Not valid id");
            throw new GeneralValidationException(List.of(message));
        }
    }

}
