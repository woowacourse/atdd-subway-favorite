import { edgeItemTemplate } from '../../utils/templates.js'
import { defaultFavorites } from '../../utils/subwayMockData.js'

function Favorite() {
  const $favoriteList = document.querySelector('#favorite-list')

  const loadFavoriteList = () => {
    const template = defaultFavorites.map(edge => edgeItemTemplate(edge)).join('')
    $favoriteList.insertAdjacentHTML('beforeend', template)
  }

  this.init = () => {
    loadFavoriteList()
  }
}

const favorite = new Favorite()
favorite.init()
