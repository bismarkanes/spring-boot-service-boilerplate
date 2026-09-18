package com.bismark.serviceboilerplate.service;

import com.bismark.serviceboilerplate.Entity.Role;
import com.bismark.serviceboilerplate.Entity.UserDetail;
import com.bismark.serviceboilerplate.dto.UserDetailDto;
import com.bismark.serviceboilerplate.error.UpdateUserException;
import com.bismark.serviceboilerplate.repository.UserDetailRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver"
})
public class AccountServiceTest {
    @MockitoBean
    UserDetailRepository userDetailRepository;

    @Autowired
    AccountService accountService;

    private UserDetail createAccountDetail(String aUsername, List<Role> roles) {
        UserDetail ud = new UserDetail();
        ud.setUsername(aUsername);
        if (roles != null)
            ud.setRoles(new HashSet<>(roles));

        return ud;
    }

    @Test
    void getAccountDetailValidTest() throws Exception {
        final String username = "bismark";
        List<UserDetail> uds = new ArrayList<>();
        uds.add(createAccountDetail(username, null));
        Mockito.when(userDetailRepository.findByUsername(username)).thenReturn(uds);
        UserDetailDto userDetailDto = accountService.getAccountDetail(username);
        Assertions.assertEquals(username, userDetailDto.getUsername());
    }

    @Test
    void getAccountDetailWithRoleValidTest() throws Exception {
        final String USERNAME = "tobby";
        final String ROLE_STR = "ROLE_MANAGER";
        Role role = Role.builder().name(ROLE_STR).build();

        List<UserDetail> uds = new ArrayList<>();
        uds.add(createAccountDetail(USERNAME, List.of(role)));
        Mockito.when(userDetailRepository.findByUsername(USERNAME)).thenReturn(uds);
        UserDetailDto userDetailDto = accountService.getAccountDetail(USERNAME);
        Assertions.assertEquals(USERNAME, userDetailDto.getUsername());
        Assertions.assertTrue(userDetailDto.getRoles().contains(ROLE_STR));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void createAccountTest() throws Exception {
        String username = "bismark";
        long returnId = 1;
        long noId = 0;

        UserDetail userDetail = new UserDetail();
        userDetail.setId(returnId);
        userDetail.setUsername(username);
        Mockito.when(userDetailRepository.save(Mockito.any(UserDetail.class))).thenReturn(userDetail);

        UserDetailDto udt = UserDetailDto.builder().id(noId).username(username).build();
        UserDetailDto udtCreate = accountService.createOrUpdateAccount(noId, udt);
        Assertions.assertEquals(returnId, udtCreate.getId());
        Assertions.assertEquals(username, udtCreate.getUsername());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateAccountTest() throws Exception {
        long id = 1;
        String username = "bismark";

        UserDetail userDetail = new UserDetail();
        userDetail.setId(id);
        userDetail.setUsername(username);
        Optional<UserDetail> optionalUserDetail = Optional.of(userDetail);

        Mockito.when(userDetailRepository.findById(id)).thenReturn(optionalUserDetail);
        Mockito.when(userDetailRepository.save(Mockito.any(UserDetail.class))).thenReturn(userDetail);

        UserDetailDto udt = UserDetailDto.builder().id(1).username(username).build();
        UserDetailDto udtUpdate = accountService.createOrUpdateAccount(id, udt);
        Assertions.assertEquals(id, udtUpdate.getId());
        Assertions.assertEquals(username, udtUpdate.getUsername());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateNoAccountTest() throws Exception {
        long id = 1;

        UserDetailDto udt = UserDetailDto.builder().id(id).build();
        Executable exec = () -> accountService.createOrUpdateAccount(id, udt);
        Assertions.assertThrows(UpdateUserException.class, exec);
    }
}
