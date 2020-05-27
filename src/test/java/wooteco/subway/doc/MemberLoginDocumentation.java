package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

/**
 *    class description
 *
 *    @author HyungJu An
 */
public class MemberLoginDocumentation {
	public static RestDocumentationResultHandler createToken() {
		return document("oauth/token",
			requestFields(
				fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address."),
				fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password.")
			),
			responseFields(
				fieldWithPath("accessToken").type(JsonFieldType.STRING).description("The user's access token."),
				fieldWithPath("tokenType").type(JsonFieldType.STRING).description("The token's type.")
			)
		);
	}

	public static RestDocumentationResultHandler getMemberOfMineBasic() {
		return document("me",
			requestHeaders(
				headerWithName("Authorization").description("The token for login which is Bearer Type.")
			),
			responseFields(
				fieldWithPath("id").type(JsonFieldType.NUMBER).description("The user's id."),
				fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address."),
				fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name.")
			)
		);
	}

	public static RestDocumentationResultHandler updateMemberOfMineBasic() {
		return document("me-edit",
			requestHeaders(
				headerWithName("Authorization").description("The token for login which is Bearer Type.")
			),
			requestFields(
				fieldWithPath("name").description("The name for update."),
				fieldWithPath("password").description("The password for update.")
			),
			responseHeaders()
		);
	}

	public static RestDocumentationResultHandler deleteMemberOfMineBasic() {
		return document("me-delete",
			requestHeaders(
				headerWithName("Authorization").description("The token for login which is Bearer Type.")
			),
			responseHeaders()
		);
	}

	public static RestDocumentationResultHandler addFavorite() {
		return document("favorite-add",
			requestHeaders(
				headerWithName("Authorization").description("The token for login which is Bearer Type.")
			),
			requestFields(
				fieldWithPath("sourceId").type(JsonFieldType.STRING)
					.description("The departure station ID of Favorite."),
				fieldWithPath("targetId").type(JsonFieldType.STRING).description("The arrival station ID of Favorite.")
			),
			responseHeaders());
	}

	public static RestDocumentationResultHandler retrieveFavorites() {
		return document("favorite-retrieve",
			requestHeaders(
				headerWithName("Authorization").description("The token for login which is Bearer Type.")
			),
			responseFields(
				fieldWithPath("[].sourceStationId").type(JsonFieldType.NUMBER)
					.description("The departure station ID of Favorite."),
				fieldWithPath("[].targetStationId").type(JsonFieldType.NUMBER)
					.description("The arrival station ID of Favorite."),
				fieldWithPath("[].sourceStationName").type(JsonFieldType.STRING)
					.description("The departure station ID of Favorite."),
				fieldWithPath("[].targetStationName").type(JsonFieldType.STRING)
					.description("The arrival station name of Favorite.")
			));
	}
}
