import {edgeItemTemplate} from "../../utils/templates.js";
import api from "../../api/index.js";
import showSnackbar from "../../lib/snackbar/index.js";
import {ERROR_MESSAGE, EVENT_TYPE, SUCCESS_MESSAGE} from "../../utils/constants.js";

function Favorite() {
    const $favoriteList = document.querySelector("#favorite-list");
    const initFavoriteList = async () => {
        try {
            const template = await api.favorite
                .get(localStorage.getItem("memberId"))
                .then(favorites =>
                    favorites["favorites"].map(edge => edgeItemTemplate(edge)).join("")
                );
            $favoriteList.innerHTML = template;
        } catch (e) {
            showSnackbar(ERROR_MESSAGE.COMMON);
        }
    };
    const onDeleteHandler = async event => {
        const $target = event.target;
        const isDeleteButton = $target.classList.contains("mdi-delete");
        if (!isDeleteButton) {
            return;
        }
        try {
            const memberId = $target.closest(".edge-item").dataset.memberId;
            const sourceId = $target.closest(".edge-item").dataset.sourceId;
            const targetId = $target.closest(".edge-item").dataset.targetId;
            await api.favorite.delete(memberId, sourceId, targetId);
            await initFavoriteList();
            showSnackbar(SUCCESS_MESSAGE.COMMON);
        } catch (e) {
            showSnackbar(ERROR_MESSAGE.COMMON);
        }
    };
    const initEventListener = () => {
        $favoriteList.addEventListener(EVENT_TYPE.CLICK, onDeleteHandler);
    };
    this.init = () => {
        initFavoriteList();
        initEventListener();
    };
}

const favorite = new Favorite();
favorite.init();