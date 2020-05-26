package wooteco.subway.doc;


import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

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

    public static ResultHandler getMemberByEmail() {
        return document("members/findByEmail",
                requestParameters(
                        parameterWithName("email").description("The user's email")
                ),
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
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

    public static RestDocumentationResultHandler createFavorite() {
        return document("members/favorite/create",
                requestFields(
                        fieldWithPath("startStationId").type(JsonFieldType.STRING).description("Start Station Id"),
                        fieldWithPath("endStationId").type(JsonFieldType.STRING).description("End Station Id")
                ),
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                )
        );
    }
}
