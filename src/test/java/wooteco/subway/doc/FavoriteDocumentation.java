package wooteco.subway.doc;

import org.springframework.test.web.servlet.ResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class FavoriteDocumentation {
    public static ResultHandler select() {
        return document("favorite/select",
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                )
        );
    }
}
