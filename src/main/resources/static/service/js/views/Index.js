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

const initializeMainMenu = async () => {
  const id = localStorage.getItem("memberId")
  let isLogin = false
  if (id) {
    await api.member.find(id)
      .then(data => {
        isLogin = !!data.name;
      })
  }
  await initMainMenu(isLogin)
  await initEventListeners()
}

function SubwayApp() {
  this.init = () => {
    initializeMainMenu()
  }
}

const subwayApp = new SubwayApp()
subwayApp.init()

