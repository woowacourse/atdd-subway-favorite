import {initMainMenu} from '../../utils/templates.js'
import api from '../../api/index.js'

function SubwayApp() {
  this.init = () => {
    api.member.find()
      .then(data => {
        const isLogin = !!data.name;
        initMainMenu(isLogin)
      })
  }
}

const subwayApp = new SubwayApp()
subwayApp.init()
