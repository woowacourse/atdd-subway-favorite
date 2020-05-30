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

  const showSearchResult = async data => {
    const isHidden = $searchResultContainer.classList.contains('hidden')
    if (isHidden) {
      $searchResultContainer.classList.remove('hidden')
    }
    $searchResult.innerHTML = searchResultTemplate(data);
    if (isLogin()) {
      const myFavorites = await api.favorite.get(localStorage.getItem("token"));
      const filter = myFavorites.favoriteResponses.find(favorite => favorite.source === $departureStationName.value && favorite.target === $arrivalStationName.value);
      initSearchButton(filter);
    }
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

  function parseLocationToId(location) {
    const index = location.lastIndexOf("/") + 1;
    return location.substring(index);
  }

  function initSearchButton(filter) {
    const classList = $favoriteButton.classList
    if (filter) {
      classList.remove('mdi-star-outline')
      classList.remove('text-gray-600')
      classList.remove('bg-yellow-500')
      classList.add('mdi-star')
      classList.add('text-yellow-500')
      $favoriteButton.dataset.id = filter.id;
      return;
    }
    classList.add('mdi-star-outline')
    classList.add('text-gray-600')
    classList.add('bg-yellow-500')
    classList.remove('mdi-star')
    classList.remove('text-yellow-500')
    $favoriteButton.dataset.id = "";
  }


  const onToggleFavorite = async event => {
    event.preventDefault()
    const isFavorite = $favoriteButton.classList.contains('mdi-star')
    const classList = $favoriteButton.classList

    if (!isLogin()) {
      alert(ERROR_MESSAGE.LOGIN_REQUIRED);
      return;
    }

    if (isFavorite) {
      await api.favorite.delete(localStorage.getItem("token"), $favoriteButton.dataset.id);
      initSearchButton();
    } else {
      classList.remove('mdi-star-outline')
      classList.remove('text-gray-600')
      classList.remove('bg-yellow-500')
      classList.add('mdi-star')
      classList.add('text-yellow-500')
      const favoriteRequest = {
        source: $departureStationName.value,
        target: $arrivalStationName.value
      }
      const response = await api.favorite.create(localStorage.getItem("token"), favoriteRequest);
      $favoriteButton.dataset.id = parseLocationToId(response.headers.get("Location"));
    }
  }
  const isLogin = () => {
    return localStorage.getItem("token")
  }


  const initEventListener = () => {
    $favoriteButton.addEventListener(EVENT_TYPE.CLICK, onToggleFavorite);
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
