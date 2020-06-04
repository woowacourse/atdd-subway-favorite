import { initNavigation } from '../utils/templates.js'
import user from '../api/user.js'

function SubwayApp() {
  this.init = async () => {
    let memberInfo = null
    try {
      memberInfo = user.isLoggedIn() && await user.getInfo()
    } finally {
      initNavigation(memberInfo)
    }
  }
}

const subwayApp = new SubwayApp()
subwayApp.init()
