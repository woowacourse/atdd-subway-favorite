package wooteco.subway.infra;

public interface TokenProvider {
    String createToken(String subject);

    String getSubject(String token);
}
