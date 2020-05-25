import {edgeItemTemplate} from '../../utils/templates.js'
import api from '../../api/index.js'
import {EVENT_TYPE} from "../../utils/constants.js";

function Favorite() {
    const $favoriteList = document.querySelector('#favorite-list')

    const loadFavoriteList = async () => {
        const data = await api.favorite.retrieve()
        if (data.favoritePaths && data.favoritePaths.length === 0) {
            return;
        }
        const template = data.favoritePaths.map(edge => edgeItemTemplate(edge)).join('')
        $favoriteList.insertAdjacentHTML('beforeend', template)
    }

    this.init = () => {
        loadFavoriteList()
        $favoriteList.addEventListener(EVENT_TYPE.CLICK, async (e) => {
            if (e.target.classList.contains('mdi-delete')) {
                const pathId = e.target.closest('li').dataset.edgeId
                await api.favorite.delete(pathId)
                const data = await api.favorite.retrieve()

                const template = data.favoritePaths.map(edge => edgeItemTemplate(edge)).join('')
                $favoriteList.innerHTML = template
            }
        })
    }
}

const favorite = new Favorite()
favorite.init()
