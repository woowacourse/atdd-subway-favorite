import { edgeItemTemplate } from '../../utils/templates.js'
import user from '../../api/user.js';
import { EVENT_TYPE, SUCCESS_MESSAGE } from '../../utils/constants.js';

function Favorite() {
  const $favoriteList = document.querySelector('#favorite-list')

  const loadFavoriteList = async () => {
    const favorites = await user.getFavorites();
    const template = favorites.map(edge => edgeItemTemplate(edge)).join('')
    $favoriteList.insertAdjacentHTML('beforeend', template)
  }

  const onRemoveHandler = async event => {
    if (event.target.classList.contains("mdi-delete")) {
      const $edgeItem = event.target.closest(".edge-item")
      const sourceId = $edgeItem.dataset.sourceId
      const targetId = $edgeItem.dataset.targetId
      try {
        await user.deleteFavorite({
          sourceId,
          targetId
        })
        Snackbar.show({
          text: SUCCESS_MESSAGE.DELETE_FAVORITE,
          pos: 'bottom-center',
          showAction: false,
          duration: 2000
        })
        $edgeItem.remove()
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
  }

  this.init = () => {
    loadFavoriteList()
    $favoriteList.addEventListener(EVENT_TYPE.CLICK, onRemoveHandler)
  }
}

const favorite = new Favorite()
favorite.init()
