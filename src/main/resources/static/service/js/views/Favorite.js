import api from '../../api/index.js'
import {edgeItemTemplate} from '../../utils/templates.js'

function Favorite() {
    const $favoriteList = document.querySelector('#favorite-list');

    const loadFavoriteList = () => {
        api.favorite.get().then(async (data) => {
            const json = await data.json();
            console.log(json);
            console.log(json[0]);
            const template = json.map(edge => edgeItemTemplate(edge)).join('');
            $favoriteList.insertAdjacentHTML('beforeend', template);
        })
    };

    this.init = () => {
        loadFavoriteList()
    }
}

const favorite = new Favorite();
favorite.init();
