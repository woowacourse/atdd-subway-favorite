import { initNavigation } from "../utils/templates.js";
import api from "../api/index.js";
import { EVENT_TYPE } from '../utils/constants.js';

function SubwayApp() {
  const renderNavigation = () => {
    const jwt = localStorage.getItem("jwt");
    if (jwt) {
      api.loginMember
        .get()
        .then(member => {
          if (member) {
            initNavigation(member);
            const $logout = document.querySelector("#logout");
            $logout.addEventListener(EVENT_TYPE.CLICK, initLogoutHandler);
          }
        })
      .catch(() => initNavigation());
    } else {
      initNavigation();
    }
  };
  const initLogoutHandler = event => {
    event.preventDefault();
    localStorage.setItem("jwt", "");
    location.href = '/';
  }

  this.init = () => {
    renderNavigation();
  };
}

const subwayApp = new SubwayApp();
subwayApp.init();
