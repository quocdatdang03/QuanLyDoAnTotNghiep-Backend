package com.quanlydoantotnghiep.DoAnTotNghiep.security.jwt;

import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    private Key key() {

        // decode jwt secret key
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    // Generate jwt token:
    public String generateToken(Authentication authentication) {

        String username = authentication.getName(); // getName() -> return email (username)
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime()+jwtExpirationDate);

        String jwtToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact(); // compact() -> create jwt token

        return jwtToken;
    }

    // Get username from Jwt token
    public String getUsername(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();

        return username;
    }

    // Validate JWT token
    public boolean validateToken(String token) {

        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(token);

            return getExpiredDate(token).after(new Date());
        }
        catch(MalformedJwtException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid jwt token");
        }
        catch(ExpiredJwtException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Expired jwt token");
        }
        catch(UnsupportedJwtException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Unsupported jwt token");
        }
        catch(IllegalArgumentException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Jwt claims string is empty");
        }
    }

    public Date getExpiredDate(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration();
    }

    public Claims getClaims(String token) {

        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims;
    }
}
