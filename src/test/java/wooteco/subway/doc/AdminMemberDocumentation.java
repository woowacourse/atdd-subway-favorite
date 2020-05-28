package wooteco.subway.doc;


import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultHandler;
import sun.awt.image.IntegerComponentRaster;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class AdminMemberDocumentation {
    public static RestDocumentationResultHandler createMember() {
        return document("members/create",
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("생성할 멤버의 이메일"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("생성할 멤버의 이름"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("생성할 멤버의 비밀번호")
                )
        );
    }

    public static ResultHandler getMember() {
        return document("members/get",
                pathParameters(
                        parameterWithName("id").description("조회할 멤버 id")
                )
        );
    }

    public static RestDocumentationResultHandler updateMember() {
        return document("members/update",
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                )
        );
    }

    public static ResultHandler deleteMember() {
        return document("members/delete",
                pathParameters(
                        parameterWithName("id").description("삭제할 멤버 id")
                )
        );
    }
}