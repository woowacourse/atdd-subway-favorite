import {initMainMenu} from '../../utils/templates.js'
import api from '../../api/index.js'

const onLogoutHandler = (event) => {
  event.preventDefault()
  localStorage.clear()
  location.reload()
}

const initEventListeners = () => {
  const $logout = document.querySelector('#log-out')
  if ($logout !== null) {
    $logout.addEventListener('click', onLogoutHandler)
  }
}

const initializeMainMenu = () => {
  api.member.find()
    .then(data => {
      const isLogin = !!data.name;
      initMainMenu(isLogin)
      initEventListeners()
    })
}

function SubwayApp() {
  this.init = () => {
    initializeMainMenu()
  }
}

const subwayApp = new SubwayApp()
subwayApp.init()

