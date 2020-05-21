package wooteco.subway.doc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;

import org.springframework.test.web.servlet.ResultHandler;

public class LoginMemberDocumentation {
	public static ResultHandler login() {
		return document("/oauth/token");
	}
}
