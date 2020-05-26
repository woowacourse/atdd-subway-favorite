package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

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

    public static RestDocumentationResultHandler getMember() {
        return document("members/get",
            requestHeaders(
                headerWithName("Authorization").description("The token for login which is Bearer Type")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("The user's id"),
                fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name")
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

    public static RestDocumentationResultHandler deleteMember() {
        return document("members/delete",
            requestHeaders(
                headerWithName("Authorization").description("The token for login which is Bearer Type")
            )
        );
    }

    public static RestDocumentationResultHandler addFavorite() {
        return document("favorites/add",
            requestHeaders(
                headerWithName("Authorization").description("The token for login which is Bearer Type")
            ), requestFields(
                fieldWithPath("source").type(JsonFieldType.STRING).description("The start station"),
                fieldWithPath("target").type(JsonFieldType.STRING).description("The arrive station")
            )
        );
    }

    public static RestDocumentationResultHandler getFavorites() {
        return document("favorites/get",
            requestHeaders(
                headerWithName("Authorization").description("The token for login which is Bearer Type")
            ), responseFields(
                fieldWithPath("[].id").description("id of favorite").type(String.class),
                fieldWithPath("[].source").description("The start station").type(String.class),
                fieldWithPath("[].target").description("The arrive station").type(String.class))
        );
    }

    public static RestDocumentationResultHandler deleteFavorites() {
        return document("favorites/delete", pathParameters(
            parameterWithName("id").description("The id of favorites")
            ),
            requestHeaders(
                headerWithName("Authorization").description("The token for login which is Bearer Type")
            ));
    }
}
