import api from '../../api/index.js'
import {edgeItemTemplate} from '../../utils/templates.js'
import {EVENT_TYPE} from '../../utils/constants.js'

function Favorite() {
    const $favoriteList = document.querySelector('#favorite-list');

    const loadFavoriteList = () => {
        api.favorite.get().then(async (data) => {
            const json = await data.json();
            const template = json.map(edge => edgeItemTemplate(edge)).join('');
            $favoriteList.insertAdjacentHTML('beforeend', template);
        })
    };

    const onDeleteFavorite = (event) => {
        const $target = event.target;
        if ($target.classList.contains('mdi-delete')) {
            const $listItem = $target.closest('li');
            const source = $listItem.getElementsByClassName('source-station')[0].innerText;
            const target = $listItem.getElementsByClassName('target-station')[0].innerText;
            const data = {
                source: source,
                target: target
            }
            api.favorite.delete(data).then(() => {
                $listItem.remove();
            });
        }
    };

    this.init = () => {
        loadFavoriteList();
        $favoriteList.addEventListener(EVENT_TYPE.CLICK, onDeleteFavorite);
    }
}

const favorite = new Favorite();
favorite.init();
