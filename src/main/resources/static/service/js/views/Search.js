import { ERROR_MESSAGE, EVENT_TYPE, PATH_TYPE, SUCCESS_MESSAGE } from '../../utils/constants.js'
import api from '../../api/index.js'
import user from '../../api/user.js'
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

  let sourceStationId
  let targetStationId

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

  const getSearchResult = async pathType => {
    const searchInput = {
      source: $departureStationName.value,
      target: $arrivalStationName.value,
      type: pathType
    }
    try {
      const path = await api.path.find(searchInput)
      showSearchResult(path)

      const { exist } = await user.hasFavorite({
        sourceId: path.stations[0].id,
        targetId: path.stations[path.stations.length - 1].id
      })

      exist ? setActiveOnFavorite() : setInactiveOnFavorite()

    }
    catch (error) {
      Snackbar.show({
        text: error.message,
        pos: 'bottom-center',
        showAction: false,
        duration: 2000
      })
    }
  }

  const setInactiveOnFavorite = () => {
    const { classList } = $favoriteButton
    classList.remove('mdi-star')
    classList.remove('text-yellow-500')
    classList.add('bg-yellow-500')
    classList.add('mdi-star-outline')
  }

  const setActiveOnFavorite = () => {
    const { classList } = $favoriteButton
    classList.add('mdi-star')
    classList.add('text-yellow-500')
    classList.remove('bg-yellow-500')
    classList.remove('mdi-star-outline')
  }

  const onToggleFavorite = async event => {
    event.preventDefault()
    if (!user.isLoggedIn()) {
      Snackbar.show({
        text: ERROR_MESSAGE.LOGIN_REQUIRED,
        pos: 'bottom-center',
        showAction: false,
        duration: 2000
      })
      return
    }
    const isFavorite = $favoriteButton.classList.contains('mdi-star')

    try {
      if (isFavorite) {
        await user.deleteFavorite({
          sourceId: sourceStationId,
          targetId: targetStationId
        })

        Snackbar.show({
          text: SUCCESS_MESSAGE.DELETE_FAVORITE,
          pos: 'bottom-center',
          showAction: false,
          duration: 2000
        })

        setInactiveOnFavorite()

      } else {
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

        setActiveOnFavorite()
      }
    }
    catch (error) {
      Snackbar.show({
        text: error.message,
        pos: 'bottom-center',
        showAction: false,
        duration: 2000
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
    const params = (new URL(document.location)).searchParams
    const source = params.get('source')
    const target = params.get('target')

    if (source && target) {
      $departureStationName.value = source
      $arrivalStationName.value = target
      getSearchResult(PATH_TYPE.DISTANCE)
    }
    initEventListener()
  }
}

const search = new Search()
search.init()
