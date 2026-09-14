package com.mballem.demo_park_api;

import com.mballem.demo_park_api.web.dto.UsuarioCreateDto;
import com.mballem.demo_park_api.web.dto.UsuarioResponseDTO;
import com.mballem.demo_park_api.web.dto.VagaCreateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@Sql(scripts = "/sql/vagas/vagas-insert.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/vagas/vagas-delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VagaIT {

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
    public void criarVaga_ComDadosValidos_RetornarLocationStatus201(){
        testClient
                .post()
                .uri("api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new VagaCreateDTO("A-05","LIVRE"))
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com","123456"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION);
    }

    @Test
    public void criarVaga_ComCodigoJaExistente_RetornarErrorMessageComStatus409(){
        testClient
                .post()
                .uri("api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new VagaCreateDTO("A-01","LIVRE"))
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com","123456"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("status").isEqualTo(409)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");
    }

    @Test
    public void criarVaga_ComDadosInvalidos_RetornarErrorMessageComStatus422(){
        testClient
                .post()
                .uri("api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new VagaCreateDTO("",""))
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com","123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");

        testClient
                .post()
                .uri("api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new VagaCreateDTO("A-501","FECHADA"))
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com","123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");
    }

    @Test
    public void criarVaga_ComUsuarioSemPermissao_RetornarErrorMessageComStatus403(){
        testClient
                .post()
                .uri("api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new VagaCreateDTO("A-01","LIVRE"))
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com","123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");
    }

    @Test
    public void buscarVaga_ComCodigoExistente_RetornarVagaComStatus200(){
        testClient
                .get()
                .uri("api/v1/vagas/{codigo}","A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com","123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo(10)
                .jsonPath("codigo").isEqualTo("A-01")
                .jsonPath("status").isEqualTo("LIVRE");
    }

    @Test
    public void criarVaga_ComCodigoInexistente_RetornarErrorMessageComStatus404(){
        testClient
                .get()
                .uri("api/v1/vagas/{codigo}","A-10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com","123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/vagas/A-10");
    }

    @Test
    public void buscarVaga_ComUsuarioSemPermissao_RetornarErrorMessageComStatus403(){
        testClient
                .get()
                .uri("api/v1/vagas/{codigo}","A-10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com","123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/vagas/A-10");
    }
}
