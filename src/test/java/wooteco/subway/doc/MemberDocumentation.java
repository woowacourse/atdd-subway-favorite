package wooteco.subway.doc;


import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class MemberDocumentation {
    public static RestDocumentationResultHandler createMember() {
        return document("members/create",
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                ),
                responseHeaders(
                        headerWithName("Location").description("The user's location who just created")
                )
        );
    }

    public static RestDocumentationResultHandler updateMember() {
        return document("members/update",
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                ),
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                )
        );
    }

    public static RestDocumentationResultHandler login() {
        return document("members/login",
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                ),
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                )
        );
    }

    public static RestDocumentationResultHandler deleteMember() {
        return document("members/delete",
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                )
        );
    }

    public static RestDocumentationResultHandler createFavorite() {
        return document("favorites/create",
                requestFields(
                        fieldWithPath("source").type(JsonFieldType.STRING).description("The departure station's id"),
                        fieldWithPath("target").type(JsonFieldType.STRING).description("The arrival station's id")
                ),
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                )
        );
    }

    public static RestDocumentationResultHandler findAllFavorites() {
        return document("favorites/read",
                responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("The favorite's id"),
                        fieldWithPath("[].sourceStationId").type(JsonFieldType.NUMBER).description("The departure station's id"),
                        fieldWithPath("[].targetStationId").type(JsonFieldType.NUMBER).description("The arrival station's id"),
                        fieldWithPath("[].sourceStationName").type(JsonFieldType.STRING).description("The departure station's name"),
                        fieldWithPath("[].targetStationName").type(JsonFieldType.STRING).description("The arrival station's name")
                ),
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                )
        );
    }

    public static ResultHandler removeFavorite() {
        return document("favorites/delete",
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                )
        );
    }
}
