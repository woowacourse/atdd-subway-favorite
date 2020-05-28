import {edgeItemTemplate} from '../../utils/templates.js'
import api from '../../api/index.js';
import {EVENT_TYPE, SUCCESS_MESSAGE} from '../../utils/constants.js';

function Favorite() {
  const $favoriteList = document.querySelector('#favorite-list')

  const loadFavoriteList = () => {
    api.favorite.get().then(data => {
      const template = data["favoriteStations"].map(edge => edgeItemTemplate(edge)).join('');
      $favoriteList.insertAdjacentHTML('beforeend', template)
    })
  };

  const deleteFavorite = event => {
    const $target = event.target;
    const isDeleteButton = $target.classList.contains("mdi-delete");
    if (!isDeleteButton) {
      return;
    }

    const favoriteInput = {
      source: $target.closest(".edge-item").dataset.source,
      target: $target.closest(".edge-item").dataset.target,
    };
    api.favorite.delete(favoriteInput);
    alert(SUCCESS_MESSAGE.SAVE);
    window.location="/favorite-page";
  };


    const eventListener = () => {
      $favoriteList.addEventListener(EVENT_TYPE.CLICK, deleteFavorite);
    }

    this.init = () => {
      loadFavoriteList();
      eventListener();
    }
  }

  const favorite = new Favorite()
  favorite.init()
