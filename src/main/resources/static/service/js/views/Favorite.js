import {edgeItemTemplate} from '../../utils/templates.js'
import api from '../../api/index.js'
import {ERROR_MESSAGE, EVENT_TYPE} from "../../utils/constants.js";

function Favorite() {
    const $favoriteList = document.querySelector('#favorite-list')

    const onDeleteFavoriteHandler = event => {
        const $target = event.target
        const isDeleteButton = $target.classList.contains('mdi-delete')
        if (!isDeleteButton) {
            return
        }
        const item = $target.closest('.list-item');
        const favorite = {
            sourceName: item.querySelector(".source-name").innerText,
            destinationName: item.querySelector(".destination-name").innerText
        }
        api.favorite
            .delete(favorite)
            .then(() => {
                item.remove()
            })
            .catch(error => alert((ERROR_MESSAGE[error.message] || ERROR_MESSAGE.DEFAULT_ERROR)));
    }

    const loadFavoriteList = () => {
        api.favorite.find()
            .then(favorites => {
                const template = favorites.map(edge => edgeItemTemplate(edge)).join('')
                $favoriteList.insertAdjacentHTML('beforeend', template)
            })
            .catch(error => {
                alert((ERROR_MESSAGE[error.message] || ERROR_MESSAGE.DEFAULT_ERROR));
                location.href = "/login";
            })
    }

    this.init = () => {
        loadFavoriteList()
        $favoriteList.addEventListener(EVENT_TYPE.CLICK, onDeleteFavoriteHandler)
    }
}

const favorite = new Favorite()
favorite.init()
