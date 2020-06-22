package wooteco.subway.service.member.dto;

public class MemberErrorResponse {

    private String errorMessage;

    private MemberErrorResponse() {
    }

    private MemberErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static MemberErrorResponse of(Exception e) {
        return new MemberErrorResponse(e.getMessage());
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
