package wooteco.subway.docs;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;

public class FavoriteDocumentation {

	public static RestDocumentationResultHandler createFavorite() {
		return document("user/favorite/create",
			setRequestHeaderWithAuthorization(),
			setRequestFields()
		);
	}

	public static RestDocumentationResultHandler getAllFavorites() {
		return document("user/favorite/getAll",
			setRequestHeaderWithAuthorization(),
			responseFields(
				fieldWithPath("[].source").type(JsonFieldType.STRING).description("The source station name"),
				fieldWithPath("[].target").type(JsonFieldType.STRING).description("The target station name")
			)
		);
	}

	public static RestDocumentationResultHandler deleteFavorite() {
		return document("user/favorite/delete",
			setRequestHeaderWithAuthorization(),
			setRequestFields()
		);
	}

	private static RequestHeadersSnippet setRequestHeaderWithAuthorization() {
		return requestHeaders(
			headerWithName("Authorization").description("This is token which is Bearer Type")
		);
	}

	private static RequestFieldsSnippet setRequestFields() {
		return requestFields(
			fieldWithPath("source").type(JsonFieldType.STRING).description("The source station name"),
			fieldWithPath("target").type(JsonFieldType.STRING).description("The target station name")
		);
	}

}
