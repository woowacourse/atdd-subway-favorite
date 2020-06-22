package wooteco.subway.service.member.dto;

public class ExistsResponse {
    private boolean exists;

    private ExistsResponse() {
    }

    public ExistsResponse(boolean exists) {
        this.exists = exists;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}
