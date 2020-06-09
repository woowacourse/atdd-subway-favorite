package wooteco.subway.service.favorite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;

public class InMemoryStation implements StationRepository {
    private Long autoIncrement = 1L;
    private Map<Long, Station> inMemoryStation = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <S extends Station> S save(S entity) {
        if (inMemoryStation.containsKey(entity.getId())) {
            inMemoryStation.put(entity.getId(), entity);
            return entity;
        }
        inMemoryStation.put(autoIncrement, new Station(autoIncrement, entity.getName()));
        return (S)inMemoryStation.get(autoIncrement++);
    }

    @Override
    public Optional<Station> findById(Long Id) {
        return Optional.of(inMemoryStation.get(Id));
    }

    @Override
    public Optional<Station> findByName(String stationName) {
        return inMemoryStation.values()
            .stream()
            .filter(entry -> entry.getName().equals(stationName))
            .findFirst();
    }

    @Override
    public <S extends Station> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Station> findAllById(Iterable<Long> ids) {
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
    public void delete(Station entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Station> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Station> findAll() {
        return null;
    }

    public Map<Long, Station> getInMemoryStation() {
        return inMemoryStation;
    }
}
