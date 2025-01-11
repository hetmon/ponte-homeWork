package hu.ponte.homework.pontevotehomework.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Transactional
public class JwtService {

    @Value("${secret.jwt-secret-key}")
    private String secretKey;

    public String createToken(Map<String, Object> claims, UserDetails userDetails) {
        if (userDetails instanceof hu.ponte.homework.pontevotehomework.domain.User user) {
            claims.put("role", user.getRole().name());
        }
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSiginInkey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
        return createToken(new HashMap<>(), userDetails);
    }

    public String extractUserName(String jwt) {
        return getClaim(jwt, Claims::getSubject);
    }

    public <T> T getClaim(String jwt, Function<Claims, T> claimExtractor) {
        final Claims claims = claimExtractor(jwt);
        return claimExtractor.apply(claims);
    }

    private Claims claimExtractor(String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSiginInkey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private Key getSiginInkey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
