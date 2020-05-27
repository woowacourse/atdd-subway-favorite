import {favoriteItemTemplate} from '../../utils/templates.js'
import api from "../../../service/api/index.js";
import {ERROR_MESSAGE} from '../../utils/constants.js'

function Favorite() {
  const $favoriteList = document.querySelector('#favorite-list')

  const loadFavoriteList = () => {
    api.memberWithToken
        .getMyFavorites()
        .then(favorites => {
          const totalHtml = favorites.map(favorite =>
              constructFavoriteItemTemplate(favorite)
          ).join("")
          $favoriteList.innerHTML = totalHtml
        })
        .catch(() => alert(ERROR_MESSAGE.FAVORITE_GET_FAIL))

    function constructFavoriteItemTemplate(favorite) {
      let data = api.station.get(favorite.sourceStationId).catch(e => console.log(e))
      const sourceStationName = data.name
      data = api.station.get(favorite.targetStationId).catch(e => console.log(e))
      const targetStationName = data.name
      const favoriteInfo = {
        id: favorite.id,
        sourceStationId: favorite.sourceStationId,
        targetStationId: favorite.targetStationId,
        sourceStationName: sourceStationName,
        targetStationName: targetStationName
      }
      return favoriteItemTemplate(favoriteInfo)
    }

    // edgeItemTemplate(favorite)).join('')
    // $favoriteList.insertAdjacentHTML('beforeend', template)
  }

  this.init = () => {
    loadFavoriteList()
  }
}

const favorite = new Favorite()
favorite.init()
