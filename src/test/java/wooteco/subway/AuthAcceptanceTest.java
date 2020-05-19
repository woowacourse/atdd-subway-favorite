package wooteco.subway;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Base64;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

@AutoConfigureMockMvc
public class AuthAcceptanceTest extends AcceptanceTest {

    public static final String AUTHORIZATION = "authorization";
    @Autowired MockMvc mockMvc;

    @Autowired ObjectMapper objectMapper;

    @DisplayName("Basic Auth")
    @Test
    void myInfoWithBasicAuth() throws Exception {
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        MemberResponse memberResponse = myInfoWithBasicAuth(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);
    }

    public MemberResponse myInfoWithBasicAuth(String email, String password) throws Exception {
        MvcResult result = mockMvc.perform(
            get("/me/basic").header(AUTHORIZATION, base64Encoded(email, password)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        return mapResultToMemberResponse(result);
    }

    private String base64Encoded(final String email, final String password) {
        return "BASIC " + new String(Base64.getEncoder().encode((email + ":" + password).getBytes()));
    }

    private MemberResponse mapResultToMemberResponse(final MvcResult result) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsString(), MemberResponse.class);
    }

    @DisplayName("Session")
    @Test
    void myInfoWithSession() throws Exception {
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        MemberResponse memberResponse = myInfoWithSession(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);
    }

    public MemberResponse myInfoWithSession(String email, String password) throws Exception {
        MvcResult login = mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .param("email", email)
            .param("password", password))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        MockHttpSession session = (MockHttpSession)login.getRequest().getSession();
        assertThat(session).isNotNull();

        MvcResult result = mockMvc.perform(get("/me/session")
            .session(session))
            .andDo(print())
            .andReturn();

        return mapResultToMemberResponse(result);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() throws Exception {
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        MemberResponse memberResponse = myInfoWithBearerAuth(tokenResponse);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(memberResponse.getName()).isEqualTo(TEST_USER_NAME);
    }

    public TokenResponse login(String email, String password) throws Exception {
        String body = objectMapper.writeValueAsString(new LoginRequest(email, password));
        MvcResult login = mockMvc.perform(post("/oauth/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();

        return objectMapper.readValue(login.getResponse().getContentAsString(), TokenResponse.class);
    }

    public MemberResponse myInfoWithBearerAuth(TokenResponse tokenResponse) throws Exception {
        MvcResult result = mockMvc.perform(get("/me/bearer")
            .header(AUTHORIZATION, tokenResponse)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        return mapResultToMemberResponse(result);
    }
}
