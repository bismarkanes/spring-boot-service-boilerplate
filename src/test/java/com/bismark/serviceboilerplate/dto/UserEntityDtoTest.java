package com.bismark.serviceboilerplate.dto;

import com.bismark.serviceboilerplate.Entity.Role;
import com.bismark.serviceboilerplate.Entity.UserDetail;
import com.bismark.serviceboilerplate.repository.RoleRepository;
import com.bismark.serviceboilerplate.repository.UserDetailRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Set;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver"
})
public class UserEntityDtoTest {
    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testUserDetailRole() {
        final String ROLE_NAME = "ROLE_ADMIN";
        final String USERNAME = "bismark";
        final String LASTNAME = "john";

        // test create role
        Role role = Role.builder().name(ROLE_NAME).build();
        Role createdRole = roleRepository.save(role);
        Assertions.assertNotEquals(0, createdRole.getId());

        // test create user detail
        UserDetail ud = new UserDetail();
        ud.setUsername(USERNAME);
        ud.setLastname(LASTNAME);
        ud.setRoles(Set.of(role));
        UserDetail createdUd = userDetailRepository.save(ud);
        Assertions.assertNotEquals(0, createdUd.getId());

        // test mapFromUserDetail
        UserDetailDto userDetailDto = UserDetailDto.mapFromUserDetail(createdUd);
        Assertions.assertEquals(USERNAME, userDetailDto.getUsername());
        Assertions.assertEquals(LASTNAME, userDetailDto.getLastname());
        Assertions.assertTrue(userDetailDto.getRoles().contains(ROLE_NAME));


        UserDetail newUserDetail = new UserDetail();
        userDetailDto.mapToUserDetail(newUserDetail);
        Assertions.assertEquals(LASTNAME, newUserDetail.getLastname());
        Assertions.assertEquals(USERNAME, newUserDetail.getUsername());
        Assertions.assertFalse(newUserDetail.getRoles().stream().filter(r -> r.getName().equals(ROLE_NAME)).toList().isEmpty());
    }
}
