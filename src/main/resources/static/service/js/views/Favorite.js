import {favoriteItemTemplate} from '../../utils/templates.js'
import api from '../../api/index.js'
import {EVENT_TYPE} from '../../utils/constants.js'

function Favorite() {
  const $favoriteList = document.querySelector('#favorite-list')

  const loadFavoriteList = () => {
    api.favorite.get().then(data => {
      data.json().then(jsonData => {
        const template = jsonData.map(edge => favoriteItemTemplate(edge)).join('')
        $favoriteList.insertAdjacentHTML('beforeend', template)
      })
    })
  }

  const deleteFavorite = (event) => {
    console.log("안녕");
    const $target = event.target
    const $EdgeItem = $target.closest('.edge-item')
    const isDeleteButton = $target.closest(".mdi-delete");
    if (!isDeleteButton) {
      return
    }
    const favoriteId = $EdgeItem.dataset.favoriteId;
    api.favorite
      .delete(favoriteId).then(res => {
      if (res.ok) {
        $EdgeItem.remove()
      }
    })
      .catch(error => {
        alert(error)
      })
  }

  this.init = () => {
    loadFavoriteList()
    $favoriteList.addEventListener(EVENT_TYPE.CLICK, deleteFavorite);
  }
}

const favorite = new Favorite()
favorite.init()
