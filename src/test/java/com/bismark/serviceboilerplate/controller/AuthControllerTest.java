package com.bismark.serviceboilerplate.controller;

import com.bismark.serviceboilerplate.dto.JwtAccessTokenDto;
import com.bismark.serviceboilerplate.dto.RequestAuthLoginDto;
import com.bismark.serviceboilerplate.dto.ResponseAuthTokenDto;
import com.bismark.serviceboilerplate.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.Date;

@SpringBootTest
@AutoConfigureRestTestClient
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver"
})
public class AuthControllerTest {
    private static final int port = 8080;

    @Autowired
    RestTestClient restTestClient;

    @Autowired
    private AuthController authController;

    @MockitoBean
    private AuthService authService;

    @Test
    void testLoadAuthController() throws Exception {
        Assertions.assertNotNull(authController);
    }

    @Test
    void testAuthLogin() throws Exception {
        RequestAuthLoginDto requestAuthLoginDto = new RequestAuthLoginDto("bismark", "1234");
        JwtAccessTokenDto jwtAccessTokenDto = new JwtAccessTokenDto("eyJhbGciOiJI", new Date());
        Mockito.when(authService.accountLogin(Mockito.any())).thenReturn(jwtAccessTokenDto);
        restTestClient.post()
                .uri("http://localhost:%d/api/auth/login".formatted(port))
                .body(requestAuthLoginDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseAuthTokenDto.class);
    }
}
