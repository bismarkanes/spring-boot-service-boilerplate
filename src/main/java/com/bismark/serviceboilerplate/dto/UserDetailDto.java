package com.bismark.serviceboilerplate.dto;

import com.bismark.serviceboilerplate.Entity.Role;
import com.bismark.serviceboilerplate.Entity.UserDetail;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDto {
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private long id;
    private String username;
    private String email;
    private String phone;
    private String idNo;
    private String taxNo;
    private String profileUrl;
    private String firstname;
    private String lastname;
    private Boolean active;
    private Set<String> roles;

    /* Update relevant field from entity */
    public static UserDetailDto mapFromUserDetail(UserDetail userDetail) {
        return UserDetailDto.builder()
                .id(userDetail.getId())
                .username(userDetail.getUsername())
                .email(userDetail.getEmail())
                .phone(userDetail.getPhone())
                .idNo(userDetail.getIdNo())
                .taxNo(userDetail.getTaxNo())
                .profileUrl(userDetail.getProfileUrl())
                .firstname(userDetail.getFirstname())
                .lastname(userDetail.getLastname())
                .active(userDetail.getActive())
                .roles(userDetail.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .build();
    }

    public void mapToUserDetail(UserDetail userDetail) {
        userDetail.setUsername(username);
        userDetail.setEmail(email);
        userDetail.setPhone(phone);
        userDetail.setIdNo(idNo);
        userDetail.setTaxNo(taxNo);
        userDetail.setProfileUrl(profileUrl);
        userDetail.setFirstname(firstname);
        userDetail.setLastname(lastname);

        if (roles != null) {
            if (userDetail.getRoles().isEmpty()) {
                roles.forEach(role -> {
                    userDetail.getRoles().add(Role.builder().name(role).build());
                });
            } else {
                Map<String, Role> mapRoles = new HashMap<>();
                userDetail.getRoles().forEach(role -> {
                    mapRoles.put(role.getName(), role);
                });

                userDetail.setRoles(roles.stream().filter(role -> mapRoles.get(role) != null).map(mapRoles::get).collect(Collectors.toSet()));
            }
        }
    }
}
