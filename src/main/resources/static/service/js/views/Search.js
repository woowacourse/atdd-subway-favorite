import {ERROR_MESSAGE, EVENT_TYPE, PATH_TYPE} from '../../utils/constants.js'
import api from '../../api/index.js'
import {searchResultTemplate} from '../../utils/templates.js'

function Search() {
    const $departureStationName = document.querySelector('#departure-station-name');
    const $arrivalStationName = document.querySelector('#arrival-station-name');
    const $searchButton = document.querySelector('#search-button');
    const $searchResultContainer = document.querySelector('#search-result-container');
    const $favoriteButton = document.querySelector('#favorite-button');
    const $searchResult = document.querySelector('#search-result');
    const $shortestDistanceTab = document.querySelector('#shortest-distance-tab');
    const $minimumTimeTab = document.querySelector('#minimum-time-tab');
    const favoriteButtonClassList = $favoriteButton.classList;

    const changeFavoriteButton = () => {
        const isFavorite = $favoriteButton.classList.contains('mdi-star');
        if (isFavorite) {
            favoriteButtonClassList.remove('mdi-star-outline');
            favoriteButtonClassList.remove('text-gray-600');
            favoriteButtonClassList.remove('bg-yellow-500');
            favoriteButtonClassList.add('mdi-star');
            favoriteButtonClassList.add('text-yellow-500');
        } else {
            favoriteButtonClassList.add('mdi-star-outline');
            favoriteButtonClassList.add('text-gray-600');
            favoriteButtonClassList.add('bg-yellow-500');
            favoriteButtonClassList.remove('mdi-star');
            favoriteButtonClassList.remove('text-yellow-500')
        }
    }

    const makeFavoriteButton = () => {
        api.favorite.get().then(async (response) => {
            const json = await response.json();
            $favoriteButton.classList.remove("mdi-star");
            json.filter(i => {
                if (i.source === $departureStationName.value && (i.target === $arrivalStationName.value)) {
                    $favoriteButton.classList.add("mdi-star");
                }
            });
            changeFavoriteButton();
        })
    }

    const showSearchResult = data => {
        const isHidden = $searchResultContainer.classList.contains('hidden');
        if (isHidden) {
            $searchResultContainer.classList.remove('hidden')
        }
        $searchResult.innerHTML = searchResultTemplate(data)
    };

    const onSearchShortestDistance = event => {
        event.preventDefault();
        $shortestDistanceTab.classList.add('active-tab');
        $minimumTimeTab.classList.remove('active-tab');
        getSearchResult(PATH_TYPE.DISTANCE)
    };

    const onSearchMinimumTime = event => {
        event.preventDefault();
        $minimumTimeTab.classList.add('active-tab');
        $shortestDistanceTab.classList.remove('active-tab');
        getSearchResult(PATH_TYPE.DURATION)
    };

    const getSearchResult = pathType => {
        const searchInput = {
            source: $departureStationName.value,
            target: $arrivalStationName.value,
            type: pathType
        };
        api.path
            .find(searchInput)
            .then(response => {
                makeFavoriteButton();
                showSearchResult(response);
            })
            .catch(error => alert(ERROR_MESSAGE.COMMON))
    };

    const onToggleFavorite = event => {
        event.preventDefault();
        const $stationList = document.querySelector('#station-list');
        const $startEndStation = $stationList.getElementsByClassName("start-end-name");

        const $startStationName = $startEndStation[0].textContent;
        const $endStationName = $startEndStation[1].textContent;

        if ($startEndStation.length != 2) {
            alert("시작역과 도착역이 올바르지 않습니다.")
            return;
        }

        const data = {
            source: $startStationName,
            target: $endStationName
        }

        const isFavorite = $favoriteButton.classList.contains('mdi-star');
        if (isFavorite) {
            api.favorite.delete(data).then(() => {
                $favoriteButton.classList.remove("mdi-star");
                changeFavoriteButton();
            });
        } else {
            api.favorite.create(data).then(() => {
                $favoriteButton.classList.add("mdi-star");
                changeFavoriteButton();
            });
        }
    };

    const initEventListener = () => {
        $favoriteButton.addEventListener(EVENT_TYPE.CLICK, onToggleFavorite);
        $searchButton.addEventListener(EVENT_TYPE.CLICK, onSearchShortestDistance);
        $shortestDistanceTab.addEventListener(EVENT_TYPE.CLICK, onSearchShortestDistance);
        $minimumTimeTab.addEventListener(EVENT_TYPE.CLICK, onSearchMinimumTime)
    };

    this.init = () => {
        initEventListener()
    }
}

const search = new Search();
search.init();
