package wooteco.subway.infra;

import java.util.Base64;
import java.util.Date;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import wooteco.subway.config.TokenConfig;

@Component
@ConfigurationPropertiesScan()
public class JwtTokenProvider {
	private final String secretKey;
	private final long validityInMilliseconds;

	public JwtTokenProvider(TokenConfig tokenConfig) {
		this.secretKey = Base64.getEncoder().encodeToString(tokenConfig.getSecretKey().getBytes());
		this.validityInMilliseconds = tokenConfig.getExpireLength();
	}

	public String createToken(String subject) {
		Claims claims = Jwts.claims().setSubject(subject);

		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
	}

	public String getSubject(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
}

