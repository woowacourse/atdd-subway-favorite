import { EVENT_TYPE, HTTP_HEADERS } from '../../utils/constants.js'
import api from '../../api/index.js'

function Login() {
  const $loginButton = document.querySelector('#login-button')
  const onLogin = async event => {
    event.preventDefault()
    const emailValue = document.querySelector('#email').value
    const passwordValue = document.querySelector('#password').value
    if (!emailValue && !passwordValue) {
      // Snackbar.show({
      //     text: ERROR_MESSAGE.LOGIN_FAIL,
      //     pos: 'bottom-center',
      //     showAction: false,
      //     duration: 2000
      // })
      alert("login fail")
      return
    }

    const loginDetail = {
      email: emailValue,
      password: passwordValue,
    }

    const response = await api.member.login(loginDetail)
    const token = response.headers.get(HTTP_HEADERS.AUTHORIZATION)
    sessionStorage.setItem(HTTP_HEADERS.AUTHORIZATION, token)
    window.location.href = "/"
  }

  this.init = () => {
    $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin)
  }
}

const login = new Login()
login.init()
