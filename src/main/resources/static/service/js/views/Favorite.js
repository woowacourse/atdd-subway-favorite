import { edgeItemTemplate } from '../../utils/templates.js'
import api from '../../api/index.js'


function Favorite() {
  const $favoriteList = document.querySelector('#favorite-list')

  const loadFavoriteList = () => {
    api.favorite.get().then(data => {
      data.json().then(jsonData => {
        const template = jsonData.map(edge => edgeItemTemplate(edge)).join('')
        $favoriteList.insertAdjacentHTML('beforeend', template)
      })
    })
  }

  this.init = () => {
    loadFavoriteList()
  }
}

const favorite = new Favorite()
favorite.init()
