package pl.dudios.debtor.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String issueToken(String subject) {
        return issueToken(subject, new HashMap<>());
    }

    public String issueToken(String subject, Long customerId, String... claims) {
        return issueToken(subject, Map.of("scope", claims, "id", customerId));
    }

    public String issueToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuer("Dudios")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(15, DAYS)))
                .signWith(getSigningKey())
                .compact();
    }

    public String getSubject(String jwt) {
        return getClaims(jwt).getSubject();
    }

    public Claims getClaims(String jwt) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public boolean isTokenValid(String jwt, String username) {
        String subject = getSubject(jwt);
        return subject.equals(username) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        return getClaims(jwt).getExpiration().before(Date.from(Instant.now()));
    }
}
