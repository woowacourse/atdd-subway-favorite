package wooteco.subway.doc;

import static org.springframework.restdocs.snippet.Attributes.*;

public interface DocumentFormatGenerator {

    static Attribute getEmailFormat() {
        return key("format").value("xxx@xxx.com");
    }
}
