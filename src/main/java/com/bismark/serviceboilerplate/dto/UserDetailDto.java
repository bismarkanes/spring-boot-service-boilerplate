package com.bismark.serviceboilerplate.dto;

import com.bismark.serviceboilerplate.Entity.UserDetail;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
                .active(userDetail.getActive()).build();
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
    }
}
