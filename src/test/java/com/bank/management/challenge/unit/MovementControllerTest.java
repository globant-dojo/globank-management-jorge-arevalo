package com.bank.management.challenge.unit;

import com.bank.management.challenge.domain.models.AccountDto;
import com.bank.management.challenge.domain.models.MovementDto;
import com.bank.management.challenge.domain.models.ClientDto;
import com.bank.management.challenge.domain.ports.services.IMovementService;
import com.bank.management.challenge.infrastructure.config.exception.GeneralValidationException;
import com.bank.management.challenge.infrastructure.rest.MovementController;
import com.bank.management.challenge.infrastructure.rest.input.MovementInput;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class MovementControllerTest {

    @Mock
    IMovementService movementService;

    @InjectMocks
    MovementController  movementController;

    private MovementDto movementDtoA;

    private List<MovementDto> movementDtoList;

    private Date actualDate;

    private UUID id;

    @BeforeEach
    public void beforeEach() {
        id = UUID.randomUUID();

        actualDate = new Date();

        movementDtoList = new ArrayList<>();

        AccountDto accountDtoA = AccountDto.builder()
                .id(UUID.randomUUID())
                .accountNumber("567898")
                .build();
        movementDtoA = MovementDto.builder()
                .id(id)
                .movementDate(actualDate)
                .movementType("Debit")
                .value(1.0)
                .balance(1.0)
                .accountDto(accountDtoA)
                .build();

        AccountDto accountDtoB = AccountDto.builder()
                .id(UUID.randomUUID())
                .accountNumber("654987")
                .build();
        MovementDto movementDtoB = MovementDto.builder()
                .id(UUID.randomUUID())
                .movementDate(new Date())
                .movementType("Credit")
                .value(1.0)
                .balance(1.0)
                .accountDto(accountDtoB)
                .build();

        movementDtoList.add(movementDtoA);
        movementDtoList.add(movementDtoB);

    }

    @Test
    void getMovementsTest() {
        Mockito.when(movementService.findAll()).thenReturn(movementDtoList);
        ResponseEntity<FormatOutput<List<MovementDto>>> response = movementController.findAll();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(1, Objects.requireNonNull(response.getBody().getMessages().size()));
        Assertions.assertEquals(2, response.getBody().getData().size());
        Assertions.assertEquals(id, response.getBody().getData().get(0).getId());
    }

    @Test
    void getMovementByIdTest() {
        Mockito.when(movementService.findById(id.toString())).thenReturn(movementDtoA);
        ResponseEntity<FormatOutput<MovementDto>> response = movementController.findById(id.toString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(1, Objects.requireNonNull(response.getBody().getMessages().size()));
        Assertions.assertEquals(id, response.getBody().getData().getId());
    }

    @Test
    void saveTest() {
        MovementInput movementInput = new MovementInput();
        movementInput.setMovementDate(actualDate);
        movementInput.setMovementType("typeTestA");
        movementInput.setValue(1.0);
        movementInput.setBalance(1.0);
        FormatInput<MovementInput> input = new FormatInput<>();
        input.setData(movementInput);

        Mockito.when(movementService.save(movementInput)).thenReturn(movementDtoA);

        ResponseEntity<FormatOutput<MovementDto>> response = movementController.save(input);

        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        Assertions.assertEquals(1, Objects.requireNonNull(response.getBody().getMessages().size()));
        Assertions.assertEquals(id, response.getBody().getData().getId());
    }

    @Test
    void updateTest() {
        MovementInput movementInput = new MovementInput();
        movementInput.setMovementDate(actualDate);
        movementInput.setMovementType("typeTestA");
        movementInput.setValue(1.0);
        movementInput.setBalance(1.0);
        FormatInput<MovementInput> input = new FormatInput<>();
        input.setData(movementInput);

        Mockito.when(movementService.update(id.toString(), movementInput)).thenReturn(movementDtoA);

        ResponseEntity<FormatOutput<MovementDto>> response = movementController.update(id.toString(), input);

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(1, Objects.requireNonNull(response.getBody().getMessages().size()));
        Assertions.assertEquals(id, response.getBody().getData().getId());
    }

    @Test
    void getNotFoundTest() {
        Mockito.when(movementService.findById(id.toString())).thenReturn(null);
        ResponseEntity<FormatOutput<MovementDto>> response = movementController.findById(id.toString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getValidationErrorTest() {
        GeneralValidationException ex = Assertions.assertThrows(GeneralValidationException.class,
                () -> {
                    ResponseEntity<FormatOutput<MovementDto>> response = movementController.findById("");
                });
        Assertions.assertEquals("Not valid id", ex.getMessages().get(0).getDetail());
    }

    @Test
    void getInternalServerErrorTest() {
        Mockito.when(movementService.findById(id.toString())).thenThrow(NullPointerException.class);
        ResponseEntity<FormatOutput<MovementDto>> response = movementController.findById(id.toString());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
