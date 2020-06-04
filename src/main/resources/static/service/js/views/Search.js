import {ERROR_SNACK_BAR, EVENT_TYPE, PATH_TYPE} from '../../utils/constants.js'
import api from '../../api/index.js'
import {searchResultTemplate} from '../../utils/templates.js'

function Search() {
    const $departureStationName = document.querySelector('#departure-station-name')
    const $arrivalStationName = document.querySelector('#arrival-station-name')
    const $searchButton = document.querySelector('#search-button')
    const $searchResultContainer = document.querySelector('#search-result-container')
    const $favoriteButton = document.querySelector('#favorite-button')
    const $searchResult = document.querySelector('#search-result')
    const $shortestDistanceTab = document.querySelector('#shortest-distance-tab')
    const $minimumTimeTab = document.querySelector('#minimum-time-tab')
    let memberId

    const loadMemberId = () => {
        api.member
            .find()
            .then(data => memberId = data.id)
    }

    const showSearchResult = data => {
        const isHidden = $searchResultContainer.classList.contains('hidden')
        if (isHidden) {
            $searchResultContainer.classList.remove('hidden')
        }

        const stations = data.stations;
        $searchResultContainer.dataset.departStationId = stations[0].id;
        $searchResultContainer.dataset.arriveStationId = stations[stations.length - 1].id;
        $searchResult.innerHTML = searchResultTemplate(data)
    }

    const onSearchShortestDistance = event => {
        event.preventDefault()
        $shortestDistanceTab.classList.add('active-tab')
        $minimumTimeTab.classList.remove('active-tab')
        getSearchResult(PATH_TYPE.DISTANCE)
    }

    const onSearchMinimumTime = event => {
        event.preventDefault()
        $minimumTimeTab.classList.add('active-tab')
        $shortestDistanceTab.classList.remove('active-tab')
        getSearchResult(PATH_TYPE.DURATION)
    }

    const getSearchResult = pathType => {
        const searchInput = {
            source: $departureStationName.value,
            target: $arrivalStationName.value,
            type: pathType
        }
        api.path
            .find(searchInput)
            .then(data => {
                showSearchResult(data)
            })
    }

    const onToggleFavorite = event => {
        event.preventDefault()
        const isFavorite = $favoriteButton.classList.contains('mdi-star')
        const classList = $favoriteButton.classList

        const data = {
            "departStationId": $searchResultContainer.dataset.departStationId,
            "arriveStationId": $searchResultContainer.dataset.arriveStationId
        }

        if (isFavorite) {
            classList.add('mdi-star-outline')
            classList.add('text-gray-600')
            classList.add('bg-yellow-500')
            classList.remove('mdi-star')
            classList.remove('text-yellow-500')
        } else {
            api.favorite
                .create(memberId, data)
                .then(response => {
                    if (!response.ok) {
                        ERROR_SNACK_BAR("FAVORITE_SAVE_FAILED");
                        return;
                    }
                    classList.remove('mdi-star-outline')
                    classList.remove('text-gray-600')
                    classList.remove('bg-yellow-500')
                    classList.add('mdi-star')
                    classList.add('text-yellow-500')
                })
        }
    }

    const initEventListener = () => {
        $favoriteButton.addEventListener(EVENT_TYPE.CLICK, onToggleFavorite)
        $searchButton.addEventListener(EVENT_TYPE.CLICK, onSearchShortestDistance)
        $shortestDistanceTab.addEventListener(EVENT_TYPE.CLICK, onSearchShortestDistance)
        $minimumTimeTab.addEventListener(EVENT_TYPE.CLICK, onSearchMinimumTime)
    }

    this.init = () => {
        loadMemberId()
        initEventListener()
    }
}

const search = new Search()
search.init()
