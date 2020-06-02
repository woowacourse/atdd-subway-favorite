package wooteco.subway.doc;

import static org.springframework.restdocs.snippet.Attributes.*;

import org.springframework.restdocs.snippet.Attributes;

public interface DocumentFormatGenerator {

    static Attributes.Attribute getEmailFormat() {
        return key("format").value("xxx@xxx.com");
    }
}
