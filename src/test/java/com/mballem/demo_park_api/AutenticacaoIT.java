package com.mballem.demo_park_api;


import com.mballem.demo_park_api.jwt.JwtToken;
import com.mballem.demo_park_api.web.dto.UsuarioCreateDto;
import com.mballem.demo_park_api.web.dto.UsuarioLoginDTO;
import com.mballem.demo_park_api.web.dto.UsuarioResponseDTO;
import com.mballem.demo_park_api.web.exception.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@Sql(scripts = "/sql/usuarios/usuarios-insert.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AutenticacaoIT {

    @LocalServerPort
    int port;
    WebTestClient testClient;
    @BeforeEach
    void setUp() {
        this.testClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    public void autenticar_ComCredenciasValidas_RetornarTokenComStatus200(){
        JwtToken responseBody = testClient
                .post()
                .uri("api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDTO("ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void autenticar_ComCredenciasInvalidas_RetornarErrorMessageComStatus400(){
        ErrorMessage responseBody = testClient
                .post()
                .uri("api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDTO("tobi@email.com", "123456"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = testClient
                .post()
                .uri("api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDTO("ana@email.com", "101010"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    public void autenticar_ComUsernameInvalidos_RetornarErrorMessageComStatus422(){
        ErrorMessage responseBody = testClient
                .post()
                .uri("api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDTO("", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

       responseBody = testClient
                .post()
                .uri("api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDTO("@email.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void autenticar_ComPasswordInvalidos_RetornarErrorMessageComStatus422(){
        ErrorMessage responseBody = testClient
                .post()
                .uri("api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDTO("ana@email.com", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDTO("ana@email.com", "123"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDTO("ana@email.com", "1234567"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }
}
