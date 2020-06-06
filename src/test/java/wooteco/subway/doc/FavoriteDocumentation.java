package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class FavoriteDocumentation {

	public static RestDocumentationResultHandler addFavorite() {
		return document("favorites/add",
			requestHeaders(
				headerWithName("Authorization").description(
					"The token for login which is Bearer Type")
			),
			requestFields(
				fieldWithPath("sourceStationId").type(JsonFieldType.NUMBER)
					.description("The favorite's source station id"),
				fieldWithPath("targetStationId").type(JsonFieldType.NUMBER)
					.description("The favorite's target station id")
			)
		);
	}

	public static RestDocumentationResultHandler getFavorites() {
		return document("favorites/read",
			requestHeaders(
				headerWithName("Authorization").description(
					"The token for login which is Bearer Type")
			),
			responseFields(
				fieldWithPath("[]").description("An array of favorites"),
				fieldWithPath("[].sourceStation").type(JsonFieldType.STRING)
					.description("The favorite's source station name"),
				fieldWithPath("[].targetStation").type(JsonFieldType.STRING)
					.description("The favorite's target station name")
			)
		);
	}

	public static RestDocumentationResultHandler deleteFavorite() {
		return document("favorites/delete",
			requestHeaders(
				headerWithName("Authorization").description(
					"The token for login which is Bearer Type")
			),
			requestFields( // Todo : 문서화도 변경 필요
				fieldWithPath("sourceStationId").type(JsonFieldType.NUMBER)
					.description("The favorite's source station id"),
				fieldWithPath("targetStationId").type(JsonFieldType.NUMBER)
					.description("The favorite's target station id")
			)
		);
	}
}
