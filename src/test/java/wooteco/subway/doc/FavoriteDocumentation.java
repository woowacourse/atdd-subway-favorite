package wooteco.subway.doc;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

/**
 * class description
 *
 * @author ParkDooWon
 */
public class FavoriteDocumentation {
	public static ResultHandler addFavorite() {
		return document("favorite/add",
				requestFields(
				fieldWithPath("departStationId").type(JsonFieldType.STRING).description("The depart station's id"),
				fieldWithPath("arriveStationId").type(JsonFieldType.STRING).description("The arrive station's id")
			),
			requestHeaders(
				headerWithName("Authorization").description("The token for login which is Bearer Type")
			)
		);
	}

	public static ResultHandler getFavorites() {
		return document("favorite/get",
			requestHeaders(
				headerWithName("Authorization").description("The token for login which is Bearer Type")
			),
			responseFields(
					fieldWithPath("[].favoriteId").description("The favorite's id"),
					fieldWithPath("[].departStationName").description("The depart station's name"),
					fieldWithPath("[].arriveStationName").description("The arrive station's name")
			)
		);
	}

	public static ResultHandler deleteFavorite() {
		return document("favorite/delete",
			requestFields(
				fieldWithPath("departStationId").type(JsonFieldType.STRING).description("The depart station's id"),
				fieldWithPath("arriveStationId").type(JsonFieldType.STRING).description("The arrive station's id")
			),
			requestHeaders(
				headerWithName("Authorization").description("The token for login which is Bearer Type")
			)
		);
	}
}
