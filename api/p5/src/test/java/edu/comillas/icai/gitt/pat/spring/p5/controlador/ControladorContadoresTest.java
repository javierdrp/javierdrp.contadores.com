package edu.comillas.icai.gitt.pat.spring.p5.controlador;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
class ControladorContadoresTest {
    private final TestRestTemplate restTemplate = new TestRestTemplate("admin@e.m", "admin");

    @Test
    public void argumentoInvalidoTest() {
        // Given ...
        String contador = "{\"nombre\":\"visitas\",\"valor\":\"abc\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // When ...
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8080/api/contadores", HttpMethod.POST,
                new HttpEntity<>(contador, headers), String.class);
        // Then ...
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void incrementoValidoTest() {
        // Given ...
        String contador = "{\"nombre\":\"test\",\"valor\":5}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.exchange(
                "http://localhost:8080/api/contadores", HttpMethod.POST,
                new HttpEntity<>(contador, headers), String.class);
        // When ...
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8080/api/contadores/test/incremento/3",
                HttpMethod.PUT, HttpEntity.EMPTY, String.class);
        // Then ...
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        String contadorIncrementado = "{\"nombre\":\"test\",\"valor\":8}";
        Assertions.assertEquals(contadorIncrementado, response.getBody());
    }

    @Test
    public void contadorExistenteTest() {
        // Given ...
        String contador = "{\"nombre\":\"visitas\",\"valor\":0}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.exchange(
                "http://localhost:8080/api/contadores", HttpMethod.POST,
                new HttpEntity<>(contador, headers), String.class);
        // When ...
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8080/api/contadores/visitas",
                HttpMethod.GET, HttpEntity.EMPTY, String.class);
        // Then ...
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(contador, response.getBody());
    }

    @Test
    public void contadorNoExistenteTest() {
        // When ...
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8080/api/contadores/no-existo",
                HttpMethod.GET, HttpEntity.EMPTY, Void.class);
        // Then ...
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}