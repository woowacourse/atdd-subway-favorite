import { EVENT_TYPE } from '../../utils/constants.js'
import api from '../../api/index.js'
import { searchResultTemplate } from '../../utils/templates.js'
import { PATH_TYPE, ERROR_MESSAGE } from '../../utils/constants.js'
import { validateLogin } from '../../login/ValidateLogin.js'

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

  const getSearchResult = async pathType => {
    const searchInput = {
      source: $departureStationName.value,
      target: $arrivalStationName.value,
      type: pathType
    }
    await api.path
      .find(searchInput)
      .then(data => showSearchResult(data))
      .catch(error => alert(ERROR_MESSAGE.COMMON))

    const token = sessionStorage.getItem("accessToken")

    let isFavorite = false;
    await api.favorite.getFavoriteByMember(token).then(data => {
      data.favorites.forEach(favorite => {
        if (favorite.startStation.name === $departureStationName.value && favorite.endStation.name === $arrivalStationName.value) {
          isFavorite = true;
          $favoriteButton.dataset.favoriteId = favorite.id
        }
      })
    })

    if (isFavorite) {
      favoriteMark()
    } else {
      notFavoriteMark()
    }
  }

  const onToggleFavorite = async event => {
    event.preventDefault()
    validateLogin()

    const startStationId = document.querySelector('#start-station-id').dataset.startStationId
    const endStationId = document.querySelector('#end-station-id').dataset.endStationId

    const token = sessionStorage.getItem("accessToken")

    let isFavorite = false;
    await api.favorite.getFavoriteByMember(token).then(data => {
      data.favorites.forEach(favorite => {
        if (favorite.startStation.name === $departureStationName.value && favorite.endStation.name === $arrivalStationName.value) {
          isFavorite = true;
          $favoriteButton.dataset.favoriteId = favorite.id
        }
      })
      if (isFavorite) {
        notFavoriteMark()
        api.favorite.delete(token, $favoriteButton.dataset.favoriteId)
      } else {
        favoriteMark()
        const favoriteInfo = {
          startStationId: startStationId,
          endStationId: endStationId
        }
        api.favorite.create(token, favoriteInfo)
      }
    })
  }

  const favoriteMark = () => {
    const classList = $favoriteButton.classList

    classList.remove('mdi-star-outline')
    classList.remove('text-gray-600')
    classList.remove('bg-yellow-500')
    classList.add('mdi-star')
    classList.add('text-yellow-500')
  }

  const notFavoriteMark = () => {
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
