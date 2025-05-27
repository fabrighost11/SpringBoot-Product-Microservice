package org.products.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtUtil {

    private static final String SECRET = "eEA6OWMWOBVncGBeP1ihsisBE9NwGecSR1uuY5JBcsm53M4Q61dx8Q3aHF5StTaHYlu7TjZ+cPHDbgxR6bG0BO7NBH60+Fxm47UayNfOkRLIHJH76voDOhgMLI82SQRVlGYavUvZOmqujoJwNXknL8lEItqNcbx30ys3LrIHkms9A3x0yMhGw/4U7lIKEOAGDgp6Z3RXh65nlEVYAG2Amg+7NQLRFlbSfZ/QJvm0WiVzxx5ZxuPvAlRGA8ZD7RPIGJSUI7vilWpjhGPKfAWzox6265iT7oHON2kYsL0pdV+pNQtRM2aSACyqUA7iW3Tj9uOx6tNUM3SZgfH2rtuVBJyXminoUcfnt8Sv60Rp1W4=";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(r -> "ROLE_" + r)
                .orElse("DEFAULT_USER");

        claims.put("role", role);

        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + 604800000))
                .setIssuedAt(new Date(now))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (JwtException e) {
            System.out.println("Firma JWT no válida");
        } catch (Exception e) {
            System.out.println("Token JWT no válido");
        }
        return false;
    }

    private String encodeSecretKey(String secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

}
