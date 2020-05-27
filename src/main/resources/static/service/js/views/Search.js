import { EVENT_TYPE, PATH_TYPE } from '../../utils/constants.js'
import api from '../../api/index.js'
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
    .then(response => {
      if (response.status === 200) {
        return response.json();
      }
      return response.json().then(error => {
        throw new Error(error.message);
      });
    }).then(data => {
      showSearchResult(data);
      const sourceStation = data.stations[0];
      const targetStation = data.stations[data.stations.length - 1];
      hasFavorite(sourceStation.id, targetStation.id);
    }).catch(error => alert(error));
  }

  const hasFavorite = (source, target) => {
    api.favorite.get(source, target)
    .then(response => {
      if (response.status === 200) {
        toggle(true);
        return response.json();
      }
      toggle(false);
      return response.json().then(error => {
        throw new Error(error.message);
      });
    }).catch(error => alert(error));
  }

  const addFavorite = () => {
    const $sourceStation = document.querySelector("#source-station");
    const $targetStation = document.querySelector("#target-station");
    const addFavoriteRequest = {
      source: $sourceStation.dataset.stationId,
      target: $targetStation.dataset.stationId,
    }
    api.favorite.add(addFavoriteRequest)
    .then(response => {
      if (response.status === 201) {
        toggle(true);
      }
      return response.json().then(error => {
        throw new Error(error.message);
      });
    }).catch(error => alert(error.message));
  }

  const removeFavorite = () => {
    const $sourceStation = document.querySelector("#source-station");
    const $targetStation = document.querySelector("#target-station");
    const removeFavoriteRequest = {
      source: $sourceStation.dataset.stationId,
      target: $targetStation.dataset.stationId,
    }
    api.favorite.remove(removeFavoriteRequest)
    .then(response => {
      if (response.status === 204) {
        toggle(false);
      }
      return response.json().then(error => {
        throw new Error(error.message);
      });
    }).catch(error => alert(error.message));
  }

  const toggle = (has) => {
    const classList = $favoriteButton.classList;

    if (has) {
      classList.remove('mdi-star-outline')
      classList.remove('text-gray-600')
      classList.remove('bg-yellow-500')
      classList.add('mdi-star')
      classList.add('text-yellow-500')
    } else {
      classList.add('mdi-star-outline')
      classList.add('text-gray-600')
      classList.add('bg-yellow-500')
      classList.remove('mdi-star')
      classList.remove('text-yellow-500')
    }
  }

  const onToggleFavorite = event => {
    if (event) {
      event.preventDefault()
    }
    const isFavorite = $favoriteButton.classList.contains('mdi-star');
    isFavorite ? removeFavorite() : addFavorite()
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
