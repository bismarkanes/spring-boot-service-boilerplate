package com.bismark.serviceboilerplate.utils;

import com.bismark.serviceboilerplate.dto.JwtAccessTokenDto;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JwtUtil {
    private static SecretKey getSecretKey () {
        String secret = System.getenv("JWT_SECRET_KEY");
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /* Default expired time of the access jwt token in minutes */
    public static long GetDefaultTokenExpireMinutes() {
        return Constants.DEFAULT_TOKEN_EXPIRE_MINUTES;
    }

    public static String generateToken(UserDetails userDetail, Date expireTime) {
        return Jwts.builder()
                .subject(userDetail.getUsername())
                .expiration(expireTime)
                .signWith(getSecretKey())
                .compact();
    }

    public static JwtAccessTokenDto generateAccessToken(UserDetails userDetail) {
        LocalDateTime expireDateTime = LocalDateTime.now().plusMinutes(JwtUtil.GetDefaultTokenExpireMinutes());
        Date expiredDate = Date.from(expireDateTime.atZone(ZoneId.systemDefault()).toInstant());
        String token = generateToken(userDetail, expiredDate);
        return new JwtAccessTokenDto(token, expiredDate);
    }

    public static String verifyToken(String token) {
        String content;

        try {
            content = Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload().getSubject();
        } catch (JwtException ex) {
            System.out.println(ex.toString());
            return null;
        }

        return content;
    }
}
