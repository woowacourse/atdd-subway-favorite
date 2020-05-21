import { initNavigation } from "../utils/templates.js";
import api from "../api";

function SubwayApp() {
  const renderNavigation = () => {
    const jwt = localStorage.getItem("jwt");
    if (jwt) {
      api.loginMember
        .get()
        .then(member => {
          if (member) {
            initNavigation(member);
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
