import { edgeItemTemplate } from '../../utils/templates.js';
import api from '../../api/index.js';
import { EVENT_TYPE } from '../../utils/constants.js';

function Favorite() {
  const $favoriteList = document.querySelector('#favorite-list')

  const loadFavoriteList = () => {
    api.favorite.getAll()
    .then(data => {
      const template = data.map(edge => edgeItemTemplate(edge)).join('')
      $favoriteList.insertAdjacentHTML('beforeend', template)
    })
  }

  const onDeleteHandler = event => {
    const $target = event.target;
    if ($target.classList.contains("mdi-delete")) {
      const $favorite = $target.closest("LI");
      const favoriteId = $favorite.dataset.favoriteId;
      api.favorite.removeById(favoriteId)
      .then(response => {
        if (response.status === 204) {
          $favorite.remove();
        }
        return response.json().then(error => {
          throw new Error(error.message);
        });
      }).catch(error => alert(error));
    }
  }

  const addEventHandler = () => {
    $favoriteList.addEventListener(EVENT_TYPE.CLICK, onDeleteHandler);
  }

  this.init = () => {
    loadFavoriteList();
    addEventHandler();
  }
}

const favorite = new Favorite()
favorite.init()
