package com.bank.management.challenge.unit;

import com.bank.management.challenge.domain.models.ClientDto;
import com.bank.management.challenge.domain.ports.services.IClientService;
import com.bank.management.challenge.infrastructure.config.exception.GeneralValidationException;
import com.bank.management.challenge.infrastructure.rest.ClientController;
import com.bank.management.challenge.infrastructure.rest.input.ClientInput;
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
class ClientControllerTest {

    @Mock
    IClientService clientService;

    @InjectMocks
    ClientController  clientController;

    private ClientDto clientDtoA;

    private List<ClientDto> clientDtoList;
    private UUID id;

    @BeforeEach
    public void beforeEach() {
        id = UUID.randomUUID();

        clientDtoList = new ArrayList<>();

        clientDtoA = ClientDto.builder()
                .id(id)
                .name("John Doe")
                .gender("Male")
                .age(25)
                .identification("identificationTest")
                .address("addressTest")
                .phoneNumber("65489678")
                .password("1234")
                .status(true)
                .build();

        ClientDto clientDtoB = ClientDto.builder()
                .id(UUID.randomUUID())
                .name("Jane Doe")
                .gender("Female")
                .age(38)
                .identification("identificationTest")
                .address("addressTest")
                .phoneNumber("65497344")
                .password("4567")
                .status(true)
                .build();

        clientDtoList.add(clientDtoA);
        clientDtoList.add(clientDtoB);

    }

    @Test
    void getClientsTest() {
        Mockito.when(clientService.findAll()).thenReturn(clientDtoList);
        ResponseEntity<FormatOutput<List<ClientDto>>> response = clientController.findAll();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(1, Objects.requireNonNull(response.getBody().getMessages().size()));
        Assertions.assertEquals(2, response.getBody().getData().size());
        Assertions.assertEquals(id, response.getBody().getData().get(0).getId());
    }

    @Test
    void getClientByIdTest() {
        Mockito.when(clientService.findById(id.toString())).thenReturn(clientDtoA);
        ResponseEntity<FormatOutput<ClientDto>> response = clientController.findById(id.toString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(1, Objects.requireNonNull(response.getBody().getMessages().size()));
        Assertions.assertEquals(id, response.getBody().getData().getId());
    }

    @Test
    void saveTest() {
        ClientInput clientInput = new ClientInput();
        clientInput.setName("John Doe");
        clientInput.setGender("Male");
        clientInput.setAge(25);
        clientInput.setIdentification("identificationTest");
        clientInput.setAddress("addressTest");
        clientInput.setPhoneNumber("65489678");
        clientInput.setPassword("1234");
        clientInput.setStatus(true);
        FormatInput<ClientInput> input = new FormatInput<>();
        input.setData(clientInput);

        Mockito.when(clientService.save(clientInput)).thenReturn(clientDtoA);

        ResponseEntity<FormatOutput<ClientDto>> response = clientController.save(input);

        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        Assertions.assertEquals(1, Objects.requireNonNull(response.getBody().getMessages().size()));
        Assertions.assertEquals(id, response.getBody().getData().getId());
    }

    @Test
    void updateTest() {
        ClientInput clientInput = new ClientInput();
        clientInput.setName("John Doe");
        clientInput.setGender("Male");
        clientInput.setAge(25);
        clientInput.setIdentification("identificationTest");
        clientInput.setAddress("addressTest");
        clientInput.setPhoneNumber("65489678");
        clientInput.setPassword("1234");
        clientInput.setStatus(true);
        FormatInput<ClientInput> input = new FormatInput<>();
        input.setData(clientInput);

        Mockito.when(clientService.update(id.toString(), clientInput)).thenReturn(clientDtoA);

        ResponseEntity<FormatOutput<ClientDto>> response = clientController.update(id.toString(), input);

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(1, Objects.requireNonNull(response.getBody().getMessages().size()));
        Assertions.assertEquals(id, response.getBody().getData().getId());
    }

    @Test
    void getNotFoundTest() {
        Mockito.when(clientService.findById(id.toString())).thenReturn(null);
        ResponseEntity<FormatOutput<ClientDto>> response = clientController.findById(id.toString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getValidationErrorTest() {
        GeneralValidationException ex = Assertions.assertThrows(GeneralValidationException.class,
                () -> {
                    ResponseEntity<FormatOutput<ClientDto>> response = clientController.findById("");
                });
        Assertions.assertEquals("Not valid id", ex.getMessages().get(0).getDetail());
    }

    @Test
    void getInternalServerErrorTest() {
        Mockito.when(clientService.findById(id.toString())).thenThrow(NullPointerException.class);
        ResponseEntity<FormatOutput<ClientDto>> response = clientController.findById(id.toString());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
