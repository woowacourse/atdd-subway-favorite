import {edgeItemTemplate} from '../../utils/templates.js'
import api from '../../api/index.js'
import {ERROR_MESSAGE, EVENT_TYPE, SUCCESS_MESSAGE} from "../../utils/constants.js"

function Favorite() {
    const $favoriteList = document.querySelector('#favorite-list')
    const accessToken = localStorage.getItem("accessToken")
    const tokenType = localStorage.getItem("tokenType")

    function createHeader() {
        return tokenType + " " + accessToken;
    }

    const loadFavoriteList = () => {
        api
            .favorite
            .find(createHeader())
            .then(async favorites => {
                const template = await favorites.map(edge => edgeItemTemplate(edge)).join('');
                $favoriteList.innerHTML = template;
            })
    }

    const onDeleteHandler = async event => {
        const $target = event.target;
        const isDeleteButton = $target.classList.contains("mdi-delete");
        if (!isDeleteButton) {
            return;
        }
        try {
            const edge = {
                "departStationId": $target.closest(".edge-item").dataset.departStationId,
                "arriveStationId": $target.closest(".edge-item").dataset.arriveStationId
            };
            await api.favorite.delete(createHeader(), edge)
            await loadFavoriteList()
            Snackbar.show({
                text: SUCCESS_MESSAGE.COMMON,
                pos: 'bottom-center',
                showAction: false,
                duration: 2000
            })
        } catch (e) {
            Snackbar.show({
                text: ERROR_MESSAGE.COMMON,
                pos: 'bottom-center',
                showAction: false,
                duration: 2000
            })
        }
    }

    const initEventListener = () => {
        $favoriteList.addEventListener(EVENT_TYPE.CLICK, onDeleteHandler)
    }

    this.init = () => {
        loadFavoriteList()
        initEventListener()
    }
}

const favorite = new Favorite();
favorite.init();