import { initNavigation } from '../utils/templates.js'
import user from '../api/user.js';

function SubwayApp() {
  this.init = async () => {
    initNavigation(user.isLoggedIn() && await user.getInfo())
  }
}

const subwayApp = new SubwayApp()
subwayApp.init()
