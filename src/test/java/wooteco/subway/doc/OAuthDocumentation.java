package wooteco.subway.doc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class OAuthDocumentation {
	public static RestDocumentationResultHandler login() {
		return document("oauth/token",
			requestFields(
				fieldWithPath("email").type(JsonFieldType.STRING).description("Input email address"),
				fieldWithPath("password").type(JsonFieldType.STRING).description("Input email address")
			),
			responseFields(
				fieldWithPath("accessToken").type(JsonFieldType.STRING).description("Authorized access Token"),
				fieldWithPath("tokenType").type(JsonFieldType.STRING).description("Authorized Token Type")
			)
		);
	}
}
