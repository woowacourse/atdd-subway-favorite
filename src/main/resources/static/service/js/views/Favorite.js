import {edgeItemTemplate} from '../../utils/templates.js'
import api from '../../api/index.js'
import {ERROR_SNACK_BAR, EVENT_TYPE, SUCCESS_SNACK_BAR} from "../../utils/constants.js"

function Favorite() {
    const $favoriteList = document.querySelector('#favorite-list')
    let memberId

    const loadMemberId = async () => {
        await api.member
            .find()
            .then(data => memberId = data.id)
    }

    const loadFavoriteList = () => {
        api.favorite
            .find(memberId)
            .then(async favorites => {
                $favoriteList.innerHTML = await favorites.map(edge => edgeItemTemplate(edge)).join('');
            })
    }

    const onDeleteHandler = async event => {
        const $target = event.target;
        const isDeleteButton = $target.classList.contains("mdi-delete");
        if (!isDeleteButton) {
            return;
        }
        try {
            const favoriteId = {
                "favoriteId": $target.closest(".edge-item").dataset.favoriteId
            };
            await api.favorite.delete(memberId, favoriteId)
            await loadFavoriteList()
            SUCCESS_SNACK_BAR("COMMON");
        } catch (e) {
            ERROR_SNACK_BAR("COMMON");
        }
    }

    const initEventListener = () => {
        $favoriteList.addEventListener(EVENT_TYPE.CLICK, onDeleteHandler)
    }

    this.init = async () => {
        await loadMemberId()
        loadFavoriteList()
        initEventListener()
    }
}

const favorite = new Favorite();
favorite.init();