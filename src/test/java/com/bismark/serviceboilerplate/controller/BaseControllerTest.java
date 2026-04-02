package com.bismark.serviceboilerplate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest
@AutoConfigureRestTestClient
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver"
})
public class BaseControllerTest {
    private static final int port = 8080;

    @Autowired
    RestTestClient restTestClient;

    @Autowired
    private BaseController baseController;

    @Test
    void testLoadBaseController() throws Exception {
        Assertions.assertNotNull(baseController);
    }

    @Test
    void testBasePublic() throws Exception {
        restTestClient.get()
                .uri("http://localhost:%d/public".formatted(port))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class);
    }

    @Test
    void testBaseRootNoUser() throws Exception {
        restTestClient.get()
                .uri("http://localhost:%d/".formatted(port))
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class);
    }

    @Test
    @WithMockUser(username = "bismark", roles = {"ADMIN"})
    void testBaseRootWithUser() throws Exception {
        restTestClient.get()
                .uri("http://localhost:%d/".formatted(port))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class);
    }
}
