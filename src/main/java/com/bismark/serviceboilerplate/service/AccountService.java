package com.bismark.serviceboilerplate.service;

import com.bismark.serviceboilerplate.dto.UserDetailDto;
import com.bismark.serviceboilerplate.Entity.UserDetail;
import com.bismark.serviceboilerplate.error.UpdateUserException;
import com.bismark.serviceboilerplate.error.UpdateUsernameException;
import com.bismark.serviceboilerplate.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    UserDetailRepository userDetailRepository;

    public UserDetailDto getAccountDetail(String username) {
        List<UserDetail> uds = userDetailRepository.findByUsername(username);

        if (uds.isEmpty()) throw new UsernameNotFoundException("");

        return UserDetailDto.mapFromUserDetail(uds.get(0));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserDetailDto createOrUpdateAccount(long id, UserDetailDto userDetailDto) {
        UserDetail userDetail;

        if (id > 0) {
            /* update record */
            Optional<UserDetail> ud = userDetailRepository.findById(id);
            if (ud.isEmpty())
                throw new UpdateUserException();

            userDetail = ud.get();
            if (!userDetail.getUsername().isBlank() && (!userDetail.getUsername().equals(userDetailDto.getUsername())))
                throw new UpdateUsernameException();
        } else {
            /* create record */
            userDetail = new UserDetail();
        }
        userDetailDto.mapToUserDetail(userDetail);

        UserDetail savedUserDetail = userDetailRepository.save(userDetail);

        return UserDetailDto.mapFromUserDetail(savedUserDetail);
    }

    public UserDetailDto createAccount(UserDetailDto userDetailDto) {
        return createOrUpdateAccount(0, userDetailDto);
    }

    public UserDetailDto updateAccount(long id, UserDetailDto userDetailDto) {
        return createOrUpdateAccount(id, userDetailDto);
    }
}
