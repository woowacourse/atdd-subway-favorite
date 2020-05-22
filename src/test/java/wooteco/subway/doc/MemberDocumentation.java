package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.ResultHandler;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class MemberDocumentation {
    public static RestDocumentationResultHandler createMember() {
        return document("members/create");
    }

    public static RestDocumentationResultHandler findMember() {
        return document("members/find");
    }

    public static RestDocumentationResultHandler updateMember() {
        return document("members/update");
    }

    public static ResultHandler deleteMember() {
        return document("members/delete");
    }
}
