package wooteco.subway.web.member.favorite;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;
import wooteco.subway.doc.FavoriteDocumentation;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.FavoriteDetail;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.favorite.FavoriteService;
import wooteco.subway.web.member.MockMvcTest;
import wooteco.subway.web.member.info.AuthInfo;
import wooteco.subway.web.member.info.UriInfo;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
public class FavoriteControllerTest extends MockMvcTest {
    private String SOURCE_TARGET_FORMAT = "{\"sourceId\":\"%s\",\"targetId\":\"%s\"}";

    @MockBean
    protected FavoriteService favoriteService;
    @MockBean
    protected MemberService memberService;

    @DisplayName("즐겨찾기 추가 컨트롤러")
    @Test
    void addFavorite() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        Favorite favorite = Favorite.of(1L, 2L);

        when(favoriteService.addFavorite(anyLong(), anyLong(), anyLong())).thenReturn(favorite);
        when(memberService.findMemberByEmail(anyString())).thenReturn(member);
        given(authorizationExtractor.extract(any(), any())).willReturn(TEST_USER_TOKEN);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        UriInfo uriInfo = UriInfo.of("/members/{id}/favorites", new Long[] {1L});
        String inputJson = String.format(SOURCE_TARGET_FORMAT, 1, 2);
        AuthInfo authInfo = AuthInfo.of(TEST_USER_TOKEN, TEST_USER_SESSION);

        postAction(uriInfo, inputJson, authInfo)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sourceId").value(1L))
                .andExpect(jsonPath("$.targetId").value(2L))
                .andDo(FavoriteDocumentation.addFavorite());
    }

    @DisplayName("즐겨찾기 조회 컨트롤러")
    @Test
    void readFavorite() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        List<FavoriteDetail> favoriteDetails = Arrays.asList(new FavoriteDetail(1L, 1L, 2L, "유안", "토니"),
                new FavoriteDetail(1L, 2L, 3L, "토니", "코즈"));

        when(favoriteService.readFavorites(anyLong())).thenReturn(favoriteDetails);
        when(memberService.findMemberByEmail(anyString())).thenReturn(member);
        given(authorizationExtractor.extract(any(), any())).willReturn(TEST_USER_TOKEN);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        UriInfo uriInfo = UriInfo.of("/members/{id}/favorites", new Long[] {1L});
        AuthInfo authInfo = AuthInfo.of(TEST_USER_TOKEN, TEST_USER_SESSION);

        getAction(uriInfo, "", authInfo)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorites").isArray())
                .andDo(FavoriteDocumentation.readFavorites());
    }

    @DisplayName("즐겨찾기 삭제 컨트롤러")
    @Test
    void removeFavorite() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        when(memberService.findMemberByEmail(anyString())).thenReturn(member);
        doNothing().when(favoriteService).removeFavorite(anyLong(), anyLong(), anyLong());
        given(authorizationExtractor.extract(any(), any())).willReturn(TEST_USER_TOKEN);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        UriInfo uriInfo = UriInfo.of("/members/{memberId}/favorites/source/{sourceId}/target/{targetId}", new Long[] {1L, 2L, 3L});
        AuthInfo authInfo = AuthInfo.of(TEST_USER_TOKEN, TEST_USER_SESSION);

        deleteAction(uriInfo, "", authInfo)
                .andExpect(status().isOk())
                .andDo(FavoriteDocumentation.deleteFavorite());
    }
}
