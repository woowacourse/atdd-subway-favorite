package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class FavoriteDocumentation {
	public static RestDocumentationResultHandler createFavorite() {
		return document("favorite/create",
			requestHeaders(
				headerWithName("Authorization").description("The token for creating favorite which is Bearer Type")
			),
			requestFields(
				fieldWithPath("source").type(JsonFieldType.STRING).description("The source station name"),
				fieldWithPath("target").type(JsonFieldType.STRING).description("The target station name")
			),
			responseHeaders(
				headerWithName("Location").description("The Favorite's location who just created")
			)
		);
	}

	public static RestDocumentationResultHandler getFavorite() {
		return document("favorite/get",
			requestHeaders(
				headerWithName("Authorization").description("The token for getting favorite which is Bearer Type")
			),
			responseFields(
				fieldWithPath("favorite[].id").type(JsonFieldType.NUMBER).description("The persist favorite id"),
				fieldWithPath("favorite[].source").type(JsonFieldType.STRING).description("The source station name"),
				fieldWithPath("favorite[].target").type(JsonFieldType.STRING).description("The target station name")
			)
		);
	}

	public static RestDocumentationResultHandler deleteFavorite() {
		return document("favorite/delete",
			requestHeaders(
				headerWithName("Authorization").description("The token for deleting favorite which is Bearer Type")
			)
		);
	}
}
