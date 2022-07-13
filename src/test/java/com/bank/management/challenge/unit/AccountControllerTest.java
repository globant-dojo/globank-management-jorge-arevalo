package com.bank.management.challenge.unit;

import com.bank.management.challenge.domain.models.AccountDto;
import com.bank.management.challenge.domain.models.ClientDto;
import com.bank.management.challenge.domain.ports.services.IAccountService;
import com.bank.management.challenge.infrastructure.config.exception.GeneralValidationException;
import com.bank.management.challenge.infrastructure.rest.AccountController;
import com.bank.management.challenge.infrastructure.rest.input.AccountInput;
import com.bank.management.challenge.infrastructure.rest.input.FormatInput;
import com.bank.management.challenge.infrastructure.rest.output.FormatOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    IAccountService accountService;

    @InjectMocks
    AccountController  accountController;

    private AccountDto accountDtoA;

    private List<AccountDto> accountDtoList;
    private UUID id;

    @BeforeEach
    public void beforeEach() {
        id = UUID.randomUUID();

        accountDtoList = new ArrayList<>();

        ClientDto clientDtoA = ClientDto.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .build();
        accountDtoA = AccountDto.builder()
                .id(id)
                .accountNumber("numberTestA")
                .accountType("typeTestA")
                .initialBalance(1.0)
                .clientDto(clientDtoA)
                .status(true)
                .build();

        ClientDto clientDtoB = ClientDto.builder()
                .id(UUID.randomUUID())
                .name("Jane Doe")
                .build();
        AccountDto accountDtoB = AccountDto.builder()
                .id(UUID.randomUUID())
                .accountNumber("numberTestB")
                .accountType("typeTestB")
                .initialBalance(1.0)
                .clientDto(clientDtoB)
                .status(true)
                .build();

        accountDtoList.add(accountDtoA);
        accountDtoList.add(accountDtoB);

    }

    @Test
    void getAccountsTest() {
        Mockito.when(accountService.findAll()).thenReturn(accountDtoList);
        ResponseEntity<FormatOutput<List<AccountDto>>> response = accountController.findAll();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(1, Objects.requireNonNull(response.getBody().getMessages().size()));
        Assertions.assertEquals(2, response.getBody().getData().size());
        Assertions.assertEquals(id, response.getBody().getData().get(0).getId());
    }

    @Test
    void getAccountByIdTest() {
        Mockito.when(accountService.findById(id.toString())).thenReturn(accountDtoA);
        ResponseEntity<FormatOutput<AccountDto>> response = accountController.findById(id.toString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(1, Objects.requireNonNull(response.getBody().getMessages().size()));
        Assertions.assertEquals(id, response.getBody().getData().getId());
    }

    @Test
    void saveTest() {
        AccountInput accountInput = new AccountInput();
        accountInput.setAccountNumber("numberTestA");
        accountInput.setAccountType("typeTestA");
        accountInput.setInitialBalance(1.0);
        accountInput.setStatus(true);
        FormatInput<AccountInput> input = new FormatInput<>();
        input.setData(accountInput);

        Mockito.when(accountService.save(accountInput)).thenReturn(accountDtoA);

        ResponseEntity<FormatOutput<AccountDto>> response = accountController.save(input);

        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        Assertions.assertEquals(1, Objects.requireNonNull(response.getBody().getMessages().size()));
        Assertions.assertEquals(id, response.getBody().getData().getId());
    }

    @Test
    void updateTest() {
        AccountInput accountInput = new AccountInput();
        accountInput.setAccountNumber("numberTestA");
        accountInput.setAccountType("typeTestA");
        accountInput.setInitialBalance(1.0);
        accountInput.setStatus(true);
        FormatInput<AccountInput> input = new FormatInput<>();
        input.setData(accountInput);

        Mockito.when(accountService.update(id.toString(), accountInput)).thenReturn(accountDtoA);

        ResponseEntity<FormatOutput<AccountDto>> response = accountController.update(id.toString(), input);

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(1, Objects.requireNonNull(response.getBody().getMessages().size()));
        Assertions.assertEquals(id, response.getBody().getData().getId());
    }

    @Test
    void getNotFoundTest() {
        Mockito.when(accountService.findById(id.toString())).thenReturn(null);
        ResponseEntity<FormatOutput<AccountDto>> response = accountController.findById(id.toString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getValidationErrorTest() {
        GeneralValidationException ex = Assertions.assertThrows(GeneralValidationException.class,
                () -> {
                    ResponseEntity<FormatOutput<AccountDto>> response = accountController.findById("");
                });
        Assertions.assertEquals("Not valid id", ex.getMessages().get(0).getDetail());
    }

    @Test
    void getInternalServerErrorTest() {
        Mockito.when(accountService.findById(id.toString())).thenThrow(NullPointerException.class);
        ResponseEntity<FormatOutput<AccountDto>> response = accountController.findById(id.toString());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
