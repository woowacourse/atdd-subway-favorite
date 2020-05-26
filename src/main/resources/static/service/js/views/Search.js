import { ERROR_MESSAGE, EVENT_TYPE, PATH_TYPE, SUCCESS_MESSAGE } from '../../utils/constants.js'
import api from '../../api/index.js'
import user from '../../api/user.js';
import { searchResultTemplate } from '../../utils/templates.js'

function Search() {
  const $departureStationName = document.querySelector('#departure-station-name')
  const $arrivalStationName = document.querySelector('#arrival-station-name')
  const $searchButton = document.querySelector('#search-button')
  const $searchResultContainer = document.querySelector('#search-result-container')
  const $favoriteButton = document.querySelector('#favorite-button')
  const $searchResult = document.querySelector('#search-result')
  const $shortestDistanceTab = document.querySelector('#shortest-distance-tab')
  const $minimumTimeTab = document.querySelector('#minimum-time-tab')

  let sourceStationId;
  let targetStationId;
  const showSearchResult = data => {
    const isHidden = $searchResultContainer.classList.contains('hidden')
    if (isHidden) {
      $searchResultContainer.classList.remove('hidden')
    }

    sourceStationId = data.stations[0].id
    targetStationId = data.stations[data.stations.length - 1].id
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
      .then(data => showSearchResult(data))
      .catch(error => alert(ERROR_MESSAGE.COMMON))
  }

  const onToggleFavorite = async event => {
    event.preventDefault()
    const isFavorite = $favoriteButton.classList.contains('mdi-star')
    const classList = $favoriteButton.classList

    try {
      await user.addFavorite({
        sourceStationId,
        targetStationId
      })

      Snackbar.show({
        text: SUCCESS_MESSAGE.ADD_FAVORITE,
        pos: 'bottom-center',
        showAction: false,
        duration: 2000
      })
    }
    catch (error) {
      Snackbar.show({
        text: error.message,
        pos: 'bottom-center',
        showAction: false,
        duration: 2000
      })
    }
    if (isFavorite) {
      classList.add('mdi-star-outline')
      classList.add('text-gray-600')
      classList.add('bg-yellow-500')
      classList.remove('mdi-star')
      classList.remove('text-yellow-500')
    } else {
      classList.remove('mdi-star-outline')
      classList.remove('text-gray-600')
      classList.remove('bg-yellow-500')
      classList.add('mdi-star')
      classList.add('text-yellow-500')
    }
  }

  const initEventListener = () => {
    $favoriteButton.addEventListener(EVENT_TYPE.CLICK, onToggleFavorite)
    $searchButton.addEventListener(EVENT_TYPE.CLICK, onSearchShortestDistance)
    $shortestDistanceTab.addEventListener(EVENT_TYPE.CLICK, onSearchShortestDistance)
    $minimumTimeTab.addEventListener(EVENT_TYPE.CLICK, onSearchMinimumTime)
  }

  this.init = () => {
    initEventListener()
  }
}

const search = new Search()
search.init()
