import { initLoginNavigation, initNavigation } from '../utils/templates.js'

function SubwayApp() {
  this.init = () => {
    if (getCookie()) {
      initLoginNavigation();
    } else {
      initNavigation();
    }
  }

  const getCookie = function () {
    const value = document.cookie.match('(^|;) ?token=([^;]*)(;|$)');
    return value ? value[2] : null;
  };
}

const subwayApp = new SubwayApp()
subwayApp.init()
