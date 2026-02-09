package com.bismark.serviceboilerplate;

import com.bismark.serviceboilerplate.Entity.UserDetail;
import com.bismark.serviceboilerplate.dto.JwtAccessTokenDto;
import com.bismark.serviceboilerplate.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.WeakKeyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver"
})
public class JwtUtilTest {
    private final String validSecret = "YEzuVqrcRTSVyBJvCwkb14,0JVGuBhnZfm";
    private final String emptySecret = "";
    private UserDetails generateUserDetails() {
        UserDetail userDetail = new UserDetail();
        userDetail.setId(1);
        userDetail.setUsername("bismark");
        return  userDetail;
    }

    private Date generateValidExpiredDate() {
        LocalDateTime expireDateTime = LocalDateTime.now().plusMinutes(JwtUtil.GetDefaultTokenExpireMinutes());
        return Date.from(expireDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Test
    void generateTokenTest() {
        String token = JwtUtil.generateToken(generateUserDetails(), generateValidExpiredDate(), validSecret);
        Assertions.assertFalse(token.isEmpty());
    }

    @Test
    void generateTokenNullSecretTest() {
        Assertions.assertThrows(WeakKeyException.class, () -> JwtUtil.generateToken(generateUserDetails(), generateValidExpiredDate(), emptySecret));
    }

    @Test
    void verifyTokenTest() {
        String token = JwtUtil.generateToken(generateUserDetails(), generateValidExpiredDate(), validSecret);
        Assertions.assertEquals("bismark", JwtUtil.verifyToken(token, validSecret));
        Assertions.assertNull(JwtUtil.verifyToken(null, validSecret));
        Assertions.assertNull(JwtUtil.verifyToken("", validSecret));
        Assertions.assertThrows(JwtException.class, () -> JwtUtil.verifyToken(token, validSecret + "."));
        Assertions.assertThrows(JwtException.class, () -> JwtUtil.verifyToken(token, emptySecret));
    }

    @Test
    void generateAccessTokenTest() {
        JwtAccessTokenDto jwtAccessTokenDto = JwtUtil.generateAccessToken(generateUserDetails(), validSecret);
        Assertions.assertFalse(jwtAccessTokenDto.getToken().isEmpty());
        Assertions.assertTrue(jwtAccessTokenDto.getExpiredDate().after(new Date()));
    }
}
