import { initNavigation } from "../utils/templates.js";
import api from "../api/index.js";
import {EVENT_TYPE} from "../utils/constants.js";

function SubwayApp() {
  const renderNavigation = () => {
    const jwt = localStorage.getItem("jwt");
    if (jwt) {
      api.loginMember
        .get()
        .then(member => {
          if (member) {
            initNavigation(member);
            document.querySelector("#logout-button").addEventListener(EVENT_TYPE.CLICK, (event) => {
              event.preventDefault();
              localStorage.setItem("jwt", "");
              location.href = "/";
            });
          }
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
