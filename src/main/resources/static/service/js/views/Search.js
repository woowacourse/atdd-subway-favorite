import {ERROR_MESSAGE, EVENT_TYPE, PATH_TYPE} from '../../utils/constants.js'
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

  const showSearchResult = data => {
    const isHidden = $searchResultContainer.classList.contains('hidden')
    if (isHidden) {
      $searchResultContainer.classList.remove('hidden')
    }
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
        .catch(error => alert((ERROR_MESSAGE[error.message] || ERROR_MESSAGE.DEFAULT_ERROR)))

    api.favorite
        .exists($departureStationName.value, $arrivalStationName.value)
        .then(result => {
          if (result.exists) {
            turnOnStar()
          } else {
            turnOffStar()
          }
        })
        .catch(error => {
          turnOffStar()
          $favoriteButton.removeEventListener(EVENT_TYPE.CLICK, onToggleFavorite);
          $favoriteButton.addEventListener(EVENT_TYPE.CLICK, unauthorizedToggleFavorite);
        })
  }

  const unauthorizedToggleFavorite = event => {
    event.preventDefault()
    if (confirm(ERROR_MESSAGE.LOGIN_NEEDED)) {
      location.href = "/login"
    }
  }

  const onToggleFavorite = event => {
    event.preventDefault()
    const isFavorite = $favoriteButton.classList.contains('mdi-star')
    const favorite = {
      sourceName: $departureStationName.value,
      destinationName: $arrivalStationName.value
    }
    if (isFavorite) {
      api.favorite.delete(favorite)
          .then(() => {
            turnOffStar();
          })
          .catch(error => {
            alert((ERROR_MESSAGE[error.message] || ERROR_MESSAGE.DEFAULT_ERROR));
            location.href = "/login";
          })
    } else {
      api.favorite.create(favorite)
          .then(() => {
            turnOnStar();
          })
          .catch(error => {
            alert((ERROR_MESSAGE[error.message] || ERROR_MESSAGE.DEFAULT_ERROR));
            location.href = "/login";
          })
    }
  }

  const turnOnStar = () => {
    const classList = $favoriteButton.classList
    classList.remove('mdi-star-outline')
    classList.remove('text-gray-600')
    classList.remove('bg-yellow-500')
    classList.add('mdi-star')
    classList.add('text-yellow-500')
  }

  const turnOffStar = () => {
    const classList = $favoriteButton.classList
    classList.add('mdi-star-outline')
    classList.add('text-gray-600')
    classList.add('bg-yellow-500')
    classList.remove('mdi-star')
    classList.remove('text-yellow-500')
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
