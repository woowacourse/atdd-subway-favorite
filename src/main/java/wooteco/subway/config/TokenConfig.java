package wooteco.subway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt.token")
public class TokenConfig {
	private String secretKey;
	private Long expireLength;

	public TokenConfig() {
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Long getExpireLength() {
		return expireLength;
	}

	public void setExpireLength(Long expireLength) {
		this.expireLength = expireLength;
	}
}
