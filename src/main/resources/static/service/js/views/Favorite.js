import api from "../../api/index.js";
import {favoriteItemTemplate} from "../../utils/templates.js";

function Favorite() {
  const $favoriteList = document.querySelector('#favorite-list')

  const loadFavoriteList = async () => {
    const favoriteItems = await api.favorite.get(localStorage.getItem("token"));
    console.log(favoriteItems);

    const template = favoriteItems.map(favorite => favoriteItemTemplate(favorite)).join('');
    $favoriteList.insertAdjacentHTML('beforeend', template)
  }

  this.init = () => {
    loadFavoriteList().then();
  }
}

const favorite = new Favorite()
favorite.init()
