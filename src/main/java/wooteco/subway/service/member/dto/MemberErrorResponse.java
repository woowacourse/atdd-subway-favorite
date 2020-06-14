package wooteco.subway.service.member.dto;

public class MemberErrorResponse {

    private String message;

    private MemberErrorResponse() {
    }

    private MemberErrorResponse(String message) {
        this.message = message;
    }

    public static MemberErrorResponse of(Exception e) {
        return new MemberErrorResponse(e.getMessage());
    }

    public String getMessage() {
        return message;
    }
}
