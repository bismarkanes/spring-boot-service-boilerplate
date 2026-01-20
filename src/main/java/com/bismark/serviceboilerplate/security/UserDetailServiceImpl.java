package com.bismark.serviceboilerplate.security;

import com.bismark.serviceboilerplate.Entity.UserDetail;
import com.bismark.serviceboilerplate.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

// create component that implements UserDetailsService
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserDetailRepository userDetailRepository;

    private static final Boolean isSimulated = false;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername username = " + username);
        if (isSimulated) {
            return User.withUsername("user").password("{noop}password").roles("user").build();
        }

        List<UserDetail> uds = userDetailRepository.findByUsername(username);

        if (uds.isEmpty()) throw new UsernameNotFoundException("User not found");

        return uds.get(0);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
