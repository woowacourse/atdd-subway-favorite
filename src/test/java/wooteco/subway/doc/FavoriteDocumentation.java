package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class FavoriteDocumentation {
    public static RestDocumentationResultHandler createFavorite() {
        return document("favorites/create",
            requestFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("The favorite's id"),
                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("The member's id"),
                fieldWithPath("source").type(JsonFieldType.NUMBER).description("The favorite's start station"),
                fieldWithPath("target").type(JsonFieldType.NUMBER).description("The favorite's arrival station")
            ),
            responseHeaders(
                headerWithName("Location").description("The favorite's location who just created")
            )
        );
    }

    public static RestDocumentationResultHandler findByEmail() {
        return document("members/find",
            requestParameters(
                parameterWithName("email").description("The user's email address")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("The user's id"),
                fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name")
            )
        );
    }
}
