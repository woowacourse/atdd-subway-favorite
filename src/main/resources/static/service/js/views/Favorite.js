import {edgeItemTemplate} from '../../utils/templates.js'
import api from '../../api/index.js'
import {EVENT_TYPE, HTTP_HEADERS} from '../../utils/constants.js'

function Favorite() {
  const $favoriteList = document.querySelector('#favorite-list')

  const token = sessionStorage.getItem(HTTP_HEADERS.AUTHORIZATION)
  let stations = []

  const loadFavoriteList = async () => {
    api.station.getAll()
        .then(data => {
          stations = [...data]
        })
    const response = await api.member.findFavorites(token)
    const responseList = response.favoriteResponses
    const favoriteItems = responseList.map(item => {
      const favoriteItem = {
        id: item.id,
        departureId: item.departureId,
        destinationId: item.destinationId,
        departureName: stations.find(station => station.id === item.departureId).name,
        destinationName: stations.find(station => station.id === item.destinationId).name
      }
      return favoriteItem
    }).map(data => edgeItemTemplate(data))
        .join('')

    $favoriteList.insertAdjacentHTML('beforeend', favoriteItems)
  }

  function onDelete(event) {
    event.preventDefault()
    const $target = event.target
    const $favoriteItem = $target.closest('.edge-item')
    const isDeleteButton = $target.classList.contains('mdi-delete')
    if (!isDeleteButton) {
      return
    }
    const favoriteId = $favoriteItem.dataset.edgeId
    api.member.deleteFavorite(token, favoriteId)
        .then(() => {
          $target.closest('.edge-item').remove()
        })
  }

  this.init = () => {
    loadFavoriteList()
    $favoriteList.addEventListener(EVENT_TYPE.CLICK, onDelete)

  }
}

const favorite = new Favorite()
favorite.init()
