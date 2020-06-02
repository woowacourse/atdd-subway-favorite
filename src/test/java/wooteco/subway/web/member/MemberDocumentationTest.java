package wooteco.subway.web.member;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.FavoriteService;
import wooteco.subway.service.member.MemberService;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = {FavoriteController.class, MemberController.class, LoginMemberController.class})
@Import({JwtTokenProvider.class, AuthorizationExtractor.class})
public abstract class MemberDocumentationTest {
	@MockBean
	protected MemberService memberService;

	@MockBean
	protected FavoriteService favoriteService;

	@Autowired
	protected JwtTokenProvider jwtTokenProvider;

	@Autowired
	protected AuthorizationExtractor authorizationExtractor;

	protected MockMvc mockMvc;
}
