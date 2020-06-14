import {edgeItemTemplate} from '../../utils/templates.js'
import api from '../../api/index.js'
import cookieApi from "../../utils/cookieApi.js";

function Favorite() {
  const $favoriteList = document.querySelector('#favorite-list')

  const loadFavoriteList = () => {
    const token = cookieApi.getCookie("token");
    api.favorite.findAll(token).then(response => {
      const template = response.map(edge => edgeItemTemplate(edge)).join('')
      $favoriteList.insertAdjacentHTML('beforeend', template)
    }).catch(error => alert("즐겨찾기 목록을 불러오는데 실패했습니다ㅠ_ㅠ"));

  };

  this.init = () => {
    loadFavoriteList()
  }
}

const favorite = new Favorite()
favorite.init()
