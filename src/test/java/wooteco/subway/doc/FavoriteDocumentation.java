package wooteco.subway.doc;

import org.springframework.test.web.servlet.ResultHandler;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class FavoriteDocumentation {
    public static ResultHandler createFavorite() {
        return document("favorites/create");
    }

    public static ResultHandler getFavorite() {
        return document("favorites/get");
    }

    public static ResultHandler deleteFavorite() {
        return document("favorites/delete");
    }
}
