import { edgeItemTemplate } from '../../utils/templates.js'
import api from '../../api/index.js'
import { EVENT_TYPE } from '../../utils/constants.js'

function Favorite() {
  const $favoriteList = document.querySelector('#favorite-list')

  const loadFavoriteList = () => {
    const token = sessionStorage.getItem("accessToken")
    api.favorite.getFavoriteByMember(token).then(data => {
      const template = data.favorites.map(edge => edgeItemTemplate(edge)).join('')
      $favoriteList.insertAdjacentHTML('beforeend', template)
    })
  }

  const onDeleteFavorite = event => {
    event.preventDefault()
    const $target = event.target
    const isDeleteButton = $target.classList.contains('mdi-delete')
    if (!isDeleteButton) {
      return
    }
    const token = sessionStorage.getItem("accessToken")
    const favoriteItem = $target.closest("#favorite-li")
    const id = favoriteItem.dataset.edgeId
    api.favorite.delete(token, id).then(() => favoriteItem.remove())
  }

  this.init = () => {
    loadFavoriteList()
    $favoriteList.addEventListener(EVENT_TYPE.CLICK, onDeleteFavorite)
  }
}

const favorite = new Favorite()
favorite.init()
