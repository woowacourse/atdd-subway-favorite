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

    public static RestDocumentationResultHandler createMemberException() {
        return document("members/createException",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
            ),
            responseFields(
                fieldWithPath("errorMessage").type(JsonFieldType.STRING).description("It's Error Message")
            )
        );
    }

    public static RestDocumentationResultHandler getMember() {
        return document("members/view",
			pathParameters(
				parameterWithName("id").description("The user's id")
			),
			responseFields(
				fieldWithPath("id").type(JsonFieldType.NUMBER).description("The user's id"),
				fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
				fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name")
			)
		);
	}

	public static RestDocumentationResultHandler getMemberException() {
		return document("members/viewException",
			pathParameters(
				parameterWithName("id").description("The user's id")
			),
			responseFields(
				fieldWithPath("errorMessage").type(JsonFieldType.STRING).description("It's Error Message")
			)
		);
	}
}
