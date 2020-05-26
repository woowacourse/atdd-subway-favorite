package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.mapper.TypeRef;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.web.dto.DefaultResponse;
import wooteco.subway.web.dto.ErrorCode;

public class MemberAcceptanceTest extends AcceptanceTest {
    @DisplayName("회원 관리 기능")
    @TestFactory
    Stream<DynamicTest> manageMember() {
        return Stream.of(
            dynamicTest(
                "회원가입", () -> {
                    String location = createMember(TEST_USER_EMAIL, TEST_USER_NAME,
                        TEST_USER_PASSWORD);
                    assertThat(location).isNotBlank();
                }),
            dynamicTest(
                "내 정보 조회", () -> {
                    TokenResponse tokenResponse = loginMember(TEST_USER_EMAIL, TEST_USER_PASSWORD);
                    MemberResponse memberResponse = getMember(tokenResponse);
                    assertThat(memberResponse.getId()).isNotNull();
                    assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
                    assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);
                }),
            dynamicTest(
                "내 정보 수정", () -> {
                    TokenResponse tokenResponse = loginMember(TEST_USER_EMAIL, TEST_USER_PASSWORD);
                    updateMember(tokenResponse);
                    MemberResponse updatedMember = getMember(tokenResponse);
                    assertThat(updatedMember.getName()).isEqualTo("NEW_" + TEST_USER_NAME);
                }),
            dynamicTest("회원 탈퇴", () -> {
                TokenResponse tokenResponse = loginMember(TEST_USER_EMAIL,
                    "NEW_" + TEST_USER_PASSWORD);
                deleteMember(tokenResponse);
            })
        );

    }

    @DisplayName("중복된 이메일로 회원가입 실패시 Exception 발생")
    @Test
    void createMember() {
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        Map<String, String> params = new HashMap<>();
        params.put("email", TEST_USER_EMAIL);
        params.put("name", TEST_USER_NAME);
        params.put("password", TEST_USER_PASSWORD);
        DefaultResponse<Void> defaultResponse = given().
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            post("/members").
            then().
            log().all().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            extract().as(new TypeRef<DefaultResponse<Void>>() {
        });

        assertThat(defaultResponse.getCode()).isEqualTo(
            ErrorCode.MEMBER_DUPLICATED_EMAIL.getCode());
        assertThat(defaultResponse.getData()).isNull();
        assertThat(defaultResponse.getMessage()).isEqualTo(
            ErrorCode.MEMBER_DUPLICATED_EMAIL.getMessage());
    }

    @DisplayName("로그인 하지 않은 상태에서 멤버 정보 조회")
    @Test
    void getMemberInfoFromUnauthorizedUser() {
        Integer code = given()
            .when()
            .get("/me")
            .then()
            .log().all()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
            .extract().as(new TypeRef<DefaultResponse<Void>>() {
            }).getCode();

        assertThat(code).isEqualTo(ErrorCode.INVALID_AUTHENTICATION.getCode());
    }
}
