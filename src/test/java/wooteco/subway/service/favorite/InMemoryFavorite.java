package wooteco.subway.service.favorite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;

public class InMemoryFavorite implements FavoriteRepository {
    private Long autoIncrement = 1L;
    private Map<Long, Favorite> inMemoryFavorite = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <S extends Favorite> S save(S entity) {
        if (inMemoryFavorite.containsKey(entity.getId())) {
            inMemoryFavorite.put(entity.getId(), entity);
            return entity;
        }
        inMemoryFavorite.put(autoIncrement,
            Favorite.of(autoIncrement, entity.getMemberId(), entity.getSourceId(), entity.getTargetId()));
        return (S)inMemoryFavorite.get(autoIncrement++);
    }

    @Override
    public List<Favorite> findByMemberId(Long memberId) {
        return inMemoryFavorite.values()
            .stream()
            .filter(values -> values.getMemberId().equals(memberId))
            .collect(Collectors.toList());
    }

    @Override
    public void deleteByMemberIdAndId(Long memberId, Long favoriteId) {
        if (isPresentFavoriteOfMember(memberId, favoriteId)) {
            inMemoryFavorite.remove(favoriteId);
        }
    }

    private boolean isPresentFavoriteOfMember(Long memberId, Long favoriteId) {
        return inMemoryFavorite.entrySet().stream()
            .anyMatch(entry -> entry.getValue().getMemberId().equals(memberId) && entry.getKey().equals(favoriteId));
    }

    @Override
    public void deleteAllByMemberId(Long memberId) {
        for (long i = 1; i < inMemoryFavorite.size() + 1; i++) {
            Favorite favorite = inMemoryFavorite.get(i);
            deleteFavoriteInMemory(memberId, i, favorite);
        }
    }

    private void deleteFavoriteInMemory(Long memberId, long i, Favorite favorite) {
        if (favorite.getMemberId().equals(memberId)) {
            inMemoryFavorite.remove(i);
        }
    }

    @Override
    public <S extends Favorite> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Favorite> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Favorite> findAll() {
        return null;
    }

    @Override
    public Iterable<Favorite> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Favorite entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Favorite> entities) {

    }

    @Override
    public void deleteAll() {

    }
}