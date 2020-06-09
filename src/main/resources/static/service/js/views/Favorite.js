import {favoriteItemTemplate} from '../../utils/templates.js'
import api from "../../../service/api/index.js";
import {ERROR_MESSAGE} from '../../utils/constants.js'

function Favorite() {
  const $favoriteList = document.querySelector('#favorite-list')

  const loadFavoriteList = () => {
    api.memberWithToken
        .getMyFavorites()
        .then(favorites => {
          $favoriteList.innerHTML = favorites.map(favorite => favoriteItemTemplate(favorite)).join("")
        })
        .catch(() => alert(ERROR_MESSAGE.FAVORITE_GET_FAIL))
  }

  const onDeleteFavoriteHandler = event => {
    const $target = event.target
    const isDeleteButton = $target.classList.contains('mdi-delete')
    if (!isDeleteButton) {
      return
    }
    api.memberWithToken
        .deleteFavorite($target.closest('.edge-item').dataset.favoriteId)
        .then((data) => {
          if (!data.ok) {
            throw new Error(data.status)
          }
          $target.closest('.edge-item').remove()
        })
        .catch(() => alert(ERROR_MESSAGE.FAVORITE_DELETE_FAIL))
  }

  const initEventListeners = () => {
    $favoriteList.addEventListener("click", onDeleteFavoriteHandler)
  }

  this.init = () => {
    if (localStorage.getItem("tokenType") === null || localStorage.getItem("accessToken") === null) {
      alert(ERROR_MESSAGE.LOGIN_FIRST)
      location.href = "/login"
      return;
    }
    loadFavoriteList()
    initEventListeners()
  }
}

const favorite = new Favorite()
favorite.init()
