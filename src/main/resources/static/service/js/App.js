import { initNavigation } from "../utils/templates.js";
import api from "../api/index.js";

function SubwayApp() {
  const renderNavigation = () => {
    const jwt = localStorage.getItem("jwt");
    if (jwt) {
      api.loginMember
      .get()
      .then(member => {
        if (member.body) {
          initNavigation(member.body.data);
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
