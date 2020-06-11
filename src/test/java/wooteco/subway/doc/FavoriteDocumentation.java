package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static wooteco.subway.doc.MemberDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class FavoriteDocumentation {

    public static RestDocumentationResultHandler createFavorite() {
        return document("favorites/create",
            requestHeaders(
                HEADER_AUTHORIZATION
            ),
            requestFields(
                fieldWithPath("preStationId").type(JsonFieldType.NUMBER)
                    .description("Departure Station's id of favorite path to create"),
                fieldWithPath("stationId").type(JsonFieldType.NUMBER)
                    .description("Arrival Station's id of favorite path to create")
            )
        );
    }

    public static RestDocumentationResultHandler readFavorites() {
        return document("favorites/read",
            requestHeaders(
                HEADER_AUTHORIZATION
            ),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                    .description("The favorite id user saved"),

                fieldWithPath("[].preStation.id").type(JsonFieldType.NUMBER)
                    .description("The departure station's id of the favorite"),
                fieldWithPath("[].preStation.name").type(JsonFieldType.STRING)
                    .description("The departure station's name of the favorite"),
                fieldWithPath("[].preStation.createdAt").type(JsonFieldType.STRING)
                    .description("The departure station's id of the favorite"),

                fieldWithPath("[].station.id").type(JsonFieldType.NUMBER)
                    .description("The arrival station's id of the favorite"),
                fieldWithPath("[].station.name").type(JsonFieldType.STRING)
                    .description("The arrival station's name of the favorite"),
                fieldWithPath("[].station.createdAt").type(JsonFieldType.STRING)
                    .description("The arrival station's created date-time of the favorite")
            )
        );
    }

    public static RestDocumentationResultHandler deleteFavorite() {
        return document("favorites/delete",
            requestHeaders(
                HEADER_AUTHORIZATION
            ),
            pathParameters(
                parameterWithName("id").description("The favorite's id to delete")
            )
        );
    }

}
