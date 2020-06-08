package wooteco.subway.web.member;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;
import static wooteco.subway.AcceptanceTest.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.TokenResponse;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(HttpEncodingAutoConfiguration.class)
public class LoginMemberControllerTest {
    @LocalServerPort
    public int port;

    @MockBean
    private MemberService memberService;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    private RequestSpecification spec;

    private TokenResponse tokenResponse;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .setPort(port)
                .build();
        RestAssured.port = port;
        BDDMockito.given(memberService.createToken(any())).willReturn(new JwtTokenProvider(secretKey, validityInMilliseconds).createToken(TEST_USER_EMAIL));
        BDDMockito.given(memberService.findMemberByEmail(TEST_USER_EMAIL)).willReturn(new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD));
        tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
    }

    @Test
    void retrieveMe() {
        //@formatter:off
        given(this.spec).
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                filter(document("me/retrieve",
                    requestHeaders(headerWithName("Authorization").description("Bearer auth credentials")))).
        when().
                get("/me").
        then().
                log().all();
        //@formatter:on
    }

    @Test
    void updateMe() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "NEW_" + TEST_USER_NAME);
        params.put("password", "NEW_" + TEST_USER_PASSWORD);
        params.put("confirmPassword", "NEW_" + TEST_USER_PASSWORD);

        //@formatter:off
        given(this.spec).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                body(params).
                filter(document("me/update",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).optional()
                                        .description("The user's name. This can be omitted."),
                                fieldWithPath("password").type(JsonFieldType.STRING).optional()
                                        .description("The user's password. This should be equal to confirmPassword. This can be omitted."),
                                fieldWithPath("confirmPassword").type(JsonFieldType.STRING).optional()
                                        .description("The user's confirm password. This should be equal to password. This can be omitted.")
                        ))).
        when().
                put("/me").
        then().
                statusCode(HttpStatus.OK.value()).
                log().all();
        //@formatter:on
    }

    @Test
    void deleteMe() {
        //@formatter:off
        given(this.spec).
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                filter(document("me/delete",
                        requestHeaders(headerWithName("Authorization").description("Bearer auth credentials")))).
        when().
                delete("/me").
        then().
                log().all();
        //@formatter:on
    }

    @Test
    void login() {
        Map<String, String> params = new HashMap<>();
        params.put("email", TEST_USER_EMAIL);
        params.put("password", TEST_USER_PASSWORD);
        //@formatter:off
        given(this.spec).
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                filter(document("login",
                                requestFields(fieldWithPath("email").description("The user's email."),
                                fieldWithPath("password").description("The user's password.")))).
        when().
                post("/oauth/token").
        then().
                log().all().
                statusCode(HttpStatus.OK.value());
        //@formatter:on
    }

    private TokenResponse login(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        //@formatter:off
        return
                given().
                        body(params).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                        post("/oauth/token").
                then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().as(TokenResponse.class);
        //@formatter:on
    }

}

