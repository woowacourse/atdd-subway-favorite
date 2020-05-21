package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class MemberDocumentation {
    public static RestDocumentationResultHandler createMember() {
        return document("members/create",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The user's email address"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password"),
                fieldWithPath("passwordCheck").type(JsonFieldType.STRING)
                    .description("The user's passwordCheck")
            ),
            responseHeaders(
                headerWithName("Location").description("The user's location who just created")
            )
        );
    }

    public static RestDocumentationResultHandler createDuplicateMember() {
        return document("members/duplicate-create",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The user's email address"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password"),
                fieldWithPath("passwordCheck").type(JsonFieldType.STRING)
                    .description("The user's passwordCheck")
            ),
            responseFields(
                fieldWithPath("message").type(JsonFieldType.STRING)
                .description("The error message")
            )
        );
    }

    public static RestDocumentationResultHandler createNotMatchPasswordMember() {
        return document("members/not-match-password-create",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The user's email address"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password"),
                fieldWithPath("passwordCheck").type(JsonFieldType.STRING)
                    .description("The user's passwordCheck")
            ),
            responseFields(
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("The error message")
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
}
