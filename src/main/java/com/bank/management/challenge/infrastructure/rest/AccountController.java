package com.bank.management.challenge.infrastructure.rest;

import com.bank.management.challenge.domain.models.AccountDto;
import com.bank.management.challenge.domain.ports.services.IAccountService;
import com.bank.management.challenge.infrastructure.config.exception.GeneralValidationException;
import com.bank.management.challenge.infrastructure.rest.input.AccountInput;
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
@RequestMapping("/accounts/v1")
@CrossOrigin
@Slf4j
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @GetMapping
    @Operation(description = "List of all accounts")
    public ResponseEntity<FormatOutput<List<AccountDto>>> findAll() {
        log.info("Getting the accounts list");
        HttpStatus status = HttpStatus.OK;
        List<AccountDto> accountDtoList = null;
        try {
            accountDtoList = accountService.findAll();
            if(accountDtoList.isEmpty()) {
                status = HttpStatus.NOT_FOUND;
            }
        } catch (IllegalArgumentException iex) {
            log.error(iex.getMessage(), iex);
            status = HttpStatus.BAD_REQUEST;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        FormatOutput<List<AccountDto>> output = new FormatOutput<>();
        output.setData(accountDtoList);
        output.setMessages(List.of(new FormatMessage(String.valueOf(status.value()), status.getReasonPhrase())));
        return new ResponseEntity<>(output, status);
    }

    @GetMapping("/{id}")
    @Operation(description = "Get account data by id")
    public ResponseEntity<FormatOutput<AccountDto>> findById(@PathVariable("id") String id) {
        validateId(id);
        log.info("Getting account with Id # {}", id);
        HttpStatus status = HttpStatus.OK;
        AccountDto accountDto = null;
        try {
            accountDto = accountService.findById(id);
            if(accountDto == null) {
                status = HttpStatus.NOT_FOUND;
            }
        } catch (IllegalArgumentException iex) {
            log.error(iex.getMessage(), iex);
            status = HttpStatus.BAD_REQUEST;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        FormatOutput<AccountDto> output = new FormatOutput<>();
        output.setData(accountDto);
        output.setMessages(List.of(new FormatMessage(String.valueOf(status.value()), status.getReasonPhrase())));
        return new ResponseEntity<>(output, status);
    }

    @PostMapping
    @Operation(description = "Save account data")
    public ResponseEntity<FormatOutput<AccountDto>> save(
            @Valid @RequestBody FormatInput<AccountInput> accountInput) {
        HttpStatus status = HttpStatus.CREATED;
        AccountDto accountDto = accountService.save(accountInput.getData());
        log.info("Saving account with Id # {}", accountDto.getId());
        FormatMessage message = new FormatMessage(String.valueOf(status.value()), status.getReasonPhrase());
        FormatOutput<AccountDto> formatOutput = new FormatOutput<>(accountDto, List.of(message));
        return new ResponseEntity<>(formatOutput, status);
    }

    @PutMapping("/{id}")
    @Operation(description = "Update account data using id")
    public ResponseEntity<FormatOutput<AccountDto>> update(
            @PathVariable("id") @NotNull String id,
            @Valid @RequestBody FormatInput<AccountInput> accountInput) {
        validateId(id);
        log.info("Updating client with Id # {}", id);
        HttpStatus status = HttpStatus.OK;
        AccountDto accountDto = accountService.update(id, accountInput.getData());
        FormatMessage message = new FormatMessage(String.valueOf(status.value()), status.getReasonPhrase());
        FormatOutput<AccountDto> formatOutput = new FormatOutput<>(accountDto, List.of(message));
        return new ResponseEntity<>(formatOutput, status);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Delete account data using id")
    public ResponseEntity<FormatOutput<AccountDto>> delete(@PathVariable("id") String id) {
        validateId(id);
        log.info("Deleting account with Id # {}", id);
        HttpStatus status = HttpStatus.OK;
        accountService.delete(id);
        FormatMessage message = new FormatMessage(String.valueOf(status.value()), status.getReasonPhrase());
        FormatOutput<AccountDto> formatOutput = new FormatOutput<>(null, List.of(message));
        return new ResponseEntity<>(formatOutput, status);
    }

    private void validateId(String id) {
        try {
            log.info("Validating account Id # {}", UUID.fromString(id));
        } catch (IllegalArgumentException iex) {
            FormatMessage message = new FormatMessage("data.id", "Not valid id");
            throw new GeneralValidationException(List.of(message));
        }
    }

}
