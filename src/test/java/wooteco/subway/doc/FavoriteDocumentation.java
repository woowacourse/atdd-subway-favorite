package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class FavoriteDocumentation {
	public static RestDocumentationResultHandler createFavorite(String identifier) {
		return document("favorite/" + identifier,
		                requestFields(
			                fieldWithPath("source").type(JsonFieldType.STRING).description("the departure " +
				                                                                               "station"),
			                fieldWithPath("target").type(JsonFieldType.STRING).description("the destination " +
				                                                                               "station")
		                ),
		                requestHeaders(
			                headerWithName("Authorization").description("the bearer token")
		                )
		);
	}

	public static RestDocumentationResultHandler retrieveFavorite(String identifier) {
		return document("favorite/" + identifier,
		                requestHeaders(
			                headerWithName("Authorization").description("the bearer token")
		                ),
		                responseFields(
			                fieldWithPath("favoritePaths[].id").type(JsonFieldType.NUMBER).description("the favorite" +
				                                                                                           " " +
				                                                                                           "path's " +
				                                                                                           "id"),
			                fieldWithPath("favoritePaths[].source.id").type(JsonFieldType.NUMBER).description("the departure station id"),
			                fieldWithPath("favoritePaths[].source.name").type(JsonFieldType.STRING).description(
				                "the departure station name"),
			                fieldWithPath("favoritePaths[].source.createdAt").type(JsonFieldType.STRING).description(
				                "the time of creating departure station"),
			                fieldWithPath("favoritePaths[].target.id").type(JsonFieldType.NUMBER).description("the destination station id"),
			                fieldWithPath("favoritePaths[].target.name").type(JsonFieldType.STRING).description(
				                "the destination station name"),
			                fieldWithPath("favoritePaths[].target.createdAt").type(JsonFieldType.STRING).description(
				                "the time of creating destination station")
		                )
		);
	}

	public static RestDocumentationResultHandler deleteFavorite(String identifier) {
		return document("favorite/" + identifier,
		                pathParameters(
			                parameterWithName("id").description("the favorite path's id to delete")
		                ),
		                requestHeaders(
			                headerWithName("Authorization").description("the bearer token")
		                )
		);
	}
}
