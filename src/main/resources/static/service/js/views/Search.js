import { ERROR_MESSAGE, EVENT_TYPE, PATH_TYPE, SUCCESS_MESSAGE } from "../../utils/constants.js";
import api from "../../api/index.js";
import { searchResultTemplate } from "../../utils/templates.js";
import showSnackbar from "../../lib/snackbar/index.js";

function Search() {
  const $departureStationName = document.querySelector(
    "#departure-station-name"
  );
  const $arrivalStationName = document.querySelector("#arrival-station-name");
  const $searchButton = document.querySelector("#search-button");
  const $searchResultContainer = document.querySelector(
    "#search-result-container"
  );
  const $favoriteButton = document.querySelector("#favorite-button");
  const $searchResult = document.querySelector("#search-result");
  const $shortestDistanceTab = document.querySelector("#shortest-distance-tab");
  const $minimumTimeTab = document.querySelector("#minimum-time-tab");
  let searchTargetStations = [];

  const showSearchResult = data => {
    const isHidden = $searchResultContainer.classList.contains("hidden");
    if (isHidden) {
      $searchResultContainer.classList.remove("hidden");
    }
    $searchResult.innerHTML = searchResultTemplate(data);
  };

  const onSearchShortestDistance = event => {
    event.preventDefault();
    $shortestDistanceTab.classList.add("active-tab");
    $minimumTimeTab.classList.remove("active-tab");
    getSearchResult(PATH_TYPE.DISTANCE);
  };

  const onSearchMinimumTime = event => {
    event.preventDefault();
    $minimumTimeTab.classList.add("active-tab");
    $shortestDistanceTab.classList.remove("active-tab");
    getSearchResult(PATH_TYPE.DURATION);
  };

  const getSearchResult = async pathType => {
    try {
      const searchInput = {
        source: $departureStationName.value,
        target: $arrivalStationName.value,
        type: pathType
      };
      const data = await api.path.find(searchInput);
      searchTargetStations = data.stations;
      showSearchResult(data);
      const jwt = localStorage.getItem("jwt");
      if (!jwt) {
        return;
      }
      const isToggled = await api.favorite.isToggled(searchTargetStations[0].id,
        searchTargetStations[searchTargetStations.length - 1].id);
      if (isToggled.existence) {
        renderToggledFavorite();
        return;
      }
      renderUnToggledFavorite();
    } catch (e) {
      showSnackbar(ERROR_MESSAGE.COMMON);
    }
  };

  const onToggleFavorite = async event => {
    event.preventDefault();
    try {
      const path = {
        sourceStationId: searchTargetStations[0].id,
        targetStationId: searchTargetStations[searchTargetStations.length - 1].id
      };
      const jwt = localStorage.getItem("jwt");
      if (!jwt) {
        return;
      }
      const isToggled = $favoriteButton.classList.contains("mdi-star");
      if (isToggled) {
        renderUnToggledFavorite();
        await api.favorite.delete(searchTargetStations[0].id,
          searchTargetStations[searchTargetStations.length - 1].id);
        showSnackbar(SUCCESS_MESSAGE.FAVORITE_REMOVE);
      } else {
        renderToggledFavorite();
        await api.favorite.create(path);
        showSnackbar(SUCCESS_MESSAGE.FAVORITE_ADD);
      }
    }
    catch (e) {
      showSnackbar(ERROR_MESSAGE.COMMON);
    }
  };

  const renderToggledFavorite = () => {
    $favoriteButton.classList.remove("mdi-star-outline");
    $favoriteButton.classList.add("mdi-star");
    $favoriteButton.classList.add("text-yellow-500");
    $favoriteButton.classList.remove("text-gray-700");
    $favoriteButton.classList.remove("bg-yellow-500");
    $favoriteButton.classList.add("bg-gray-700");
  }

  const renderUnToggledFavorite = () => {
    $favoriteButton.classList.add("mdi-star-outline");
    $favoriteButton.classList.remove("mdi-star");
    $favoriteButton.classList.remove("text-yellow-500");
    $favoriteButton.classList.add("text-gray-700");
    $favoriteButton.classList.add("bg-yellow-500");
    $favoriteButton.classList.remove("bg-gray-700");
  }

  const initEventListener = () => {
    $favoriteButton.addEventListener(EVENT_TYPE.CLICK, onToggleFavorite);
    $searchButton.addEventListener(EVENT_TYPE.CLICK, onSearchShortestDistance);
    $shortestDistanceTab.addEventListener(EVENT_TYPE.CLICK, onSearchShortestDistance);
    $minimumTimeTab.addEventListener(EVENT_TYPE.CLICK, onSearchMinimumTime);
  };

  this.init = () => {
    initEventListener();
  };
}

const search = new Search();
search.init();
