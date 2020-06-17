package wooteco.subway.web.exception.validator;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import wooteco.subway.service.favorite.dto.FavoriteRequest;

public class FavoriteRequestValidator implements
    ConstraintValidator<CheckFavoriteSameStation, FavoriteRequest> {

    @Override
    public void initialize(CheckFavoriteSameStation constraintAnnotation) {
    }

    @Override
    public boolean isValid(FavoriteRequest favoriteRequest, ConstraintValidatorContext context) {
        return !Objects.equals(favoriteRequest.getSource(), favoriteRequest.getTarget());
    }
}
