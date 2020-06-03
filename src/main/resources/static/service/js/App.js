import {initNavigation} from "../utils/templates.js";
import {EVENT_TYPE} from "../utils/constants.js"
import api from "../api/index.js";

function SubwayApp() {
    const initLogoutHandler = event => {
        event.preventDefault();
        localStorage.removeItem("jwt");
        location.href = "/";
    }

    const renderNavigation = () => {
        const jwt = localStorage.getItem("jwt");
        if (jwt) {
            api.member
                .get()
                .then(member => {
                    if (member) {
                        initNavigation(member);
                    }
                    const $logoutButton = document.querySelector("#logout");
                    $logoutButton.addEventListener(EVENT_TYPE.CLICK, initLogoutHandler);
                })
                .catch(() => initNavigation());
        } else {
            initNavigation();
        }
    };

    this.init = () => {
        renderNavigation();
    };
}

const subwayApp = new SubwayApp();
subwayApp.init();
