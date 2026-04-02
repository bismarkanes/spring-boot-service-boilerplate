package com.bismark.serviceboilerplate.controller;

import com.bismark.serviceboilerplate.dto.UserDetailDto;
import com.bismark.serviceboilerplate.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest
@AutoConfigureRestTestClient
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver"
})
public class AccountControllerTest {
    private static final int port = 8080;

    @Autowired
    RestTestClient restTestClient;

    @Autowired
    private AccountController accountController;

    @MockitoBean
    private AccountService accountService;

    @Test
    void testLoadAccountController() throws Exception {
        Assertions.assertNotNull(accountController);
    }

    @Test
    void testGetAccountDetailNoUser() throws Exception {
        restTestClient.get()
                .uri("http://localhost:%d/api/account/detail".formatted(port))
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class);
    }

    @Test
    @WithMockUser(username = "bismark", roles = {"USER"})
    void testGetAccountDetailWithUser() throws Exception {
        UserDetailDto userDetailDto = UserDetailDto.builder().id(1).username("bismark").active(true).build();
        Mockito.when(accountService.getAccountDetail(Mockito.any())).thenReturn(userDetailDto);
        restTestClient.get()
                .uri("http://localhost:%d/api/account/detail".formatted(port))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDetailDto.class);
    }

    @Test
    @WithMockUser(username = "bismark", roles = {"ADMIN"})
    void testCreateAccount() throws Exception {
        UserDetailDto userDetailDto = UserDetailDto.builder().id(2).username("auser").active(true).build();
        Mockito.when(accountService.createAccount(Mockito.any())).thenReturn(userDetailDto);
        restTestClient.post()
                .uri("http://localhost:%d/api/account".formatted(port))
                .body(userDetailDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserDetailDto.class);
    }

    @Test
    @WithMockUser(username = "bismark", roles = {"ADMIN"})
    void testUpdateAccount() throws Exception {
        long id = 2L;
        UserDetailDto userDetailDto = UserDetailDto.builder().id(id).username("auser").active(true).build();
        Mockito.when(accountService.updateAccount(Mockito.anyLong(), Mockito.any())).thenReturn(userDetailDto);
        restTestClient.patch()
                .uri("http://localhost:%d/api/account/%s".formatted(port, Long.toString(id)))
                .body(userDetailDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDetailDto.class);
    }
}
