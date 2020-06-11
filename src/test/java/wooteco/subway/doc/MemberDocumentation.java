package wooteco.subway.doc;

import org.springframework.test.web.servlet.ResultHandler;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class MemberDocumentation {
    public static ResultHandler createMember() {
        return document("members/create");
    }

    public static ResultHandler findMember() {
        return document("members/find");
    }

    public static ResultHandler updateMember() {
        return document("members/update");
    }

    public static ResultHandler deleteMember() {
        return document("members/delete");
    }
}
