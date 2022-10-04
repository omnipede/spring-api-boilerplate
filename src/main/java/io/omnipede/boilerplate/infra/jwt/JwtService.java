package io.omnipede.boilerplate.infra.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.omnipede.boilerplate.domain.auth.TokenService;
import io.omnipede.boilerplate.system.exception.ErrorCode;
import io.omnipede.boilerplate.system.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class JwtService implements TokenService {

    private final JwtConfig jwtConfig;

    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public String create(String username) {
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        return BEARER_PREFIX + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtConfig.getDuration()))
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecretKey())
                .compact();
    }

    @Override
    public Optional<String> validate(String token) {
        // 토큰 prefix 에러
        if (!token.startsWith(BEARER_PREFIX))
            return Optional.empty();

        // Bearer prefix 제외.
        String unPrefixed = token.substring(BEARER_PREFIX.length());

        // Parse JWT
        Jws<Claims> claims = getClaims(unPrefixed);

        // JWT subject (user email) 을 반환.
        String subject = claims.getBody().getSubject();
        return Optional.ofNullable(subject);
    }

    private Jws<Claims> getClaims(String token) {
        try {
            return Jwts.parser()
                .setSigningKey(jwtConfig.getSecretKey())
                .parseClaimsJws(token);
        } catch (Exception e) {
            throw new SystemException(ErrorCode.UNAUTHORIZED, e);
        }
    }
}
