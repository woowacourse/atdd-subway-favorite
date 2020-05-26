import { edgeItemTemplate } from "../../utils/templates.js";
import api from "../../api/index.js";
import showSnackbar from "../../lib/snackbar/index.js";
import { ERROR_MESSAGE, EVENT_TYPE, SUCCESS_MESSAGE } from "../../utils/constants.js";

function Favorite() {
  const $favoriteList = document.querySelector("#favorite-list");

  const initFavoriteList = async () => {
    try {
      const template = await api.favorite
      .getAll()
      .then(favorites =>
        favorites.body.data.favoriteResponses.map(favorite => edgeItemTemplate(favorite)).join("")
      );
      $favoriteList.innerHTML = template;
    }
    catch (e) {
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
      const sourceStationId = $target.closest(".edge-item").dataset.sourceStationId;
      const targetStationId = $target.closest(".edge-item").dataset.targetStationId;
      const deleteRequest = {
        "sourceStationId": sourceStationId,
        "targetStationId": targetStationId
      };
      await api.favorite.delete(deleteRequest);
      await initFavoriteList();
      showSnackbar(SUCCESS_MESSAGE.COMMON);
    }
    catch (e) {
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
