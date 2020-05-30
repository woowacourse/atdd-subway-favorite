import api from "../../api/index.js";
import {favoriteItemTemplate} from "../../utils/templates.js";
import {ERROR_MESSAGE, EVENT_TYPE} from "../../../admin/utils/constants.js";

function Favorite() {
  const $favoriteList = document.querySelector('#favorite-list')

  const onDeleteHandler = event => {
    const $target = event.target
    const isDeleteButton = $target.classList.contains('mdi-delete')
    if (!isDeleteButton) {
      return
    }

    api.favorite
      .delete(localStorage.getItem("token"), $target.closest('.list-item').dataset.id)
      .then(() => {
        $target.closest('.list-item').remove()
      })
      .catch(() => alert(ERROR_MESSAGE.COMMON))
  }

  const loadFavoriteList = async () => {
    const favoriteItems = await api.favorite.get(localStorage.getItem("token"));

    const template = favoriteItems.favoriteResponses.map(favorite => favoriteItemTemplate(favorite)).join('');
    $favoriteList.insertAdjacentHTML('beforeend', template)
  }

  this.init = () => {
    loadFavoriteList().then();
    $favoriteList.addEventListener(EVENT_TYPE.CLICK, onDeleteHandler)
  }
}

const favorite = new Favorite()
favorite.init()
