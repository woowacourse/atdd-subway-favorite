import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import api from "../../api/index.js";

function Login() {
  const $loginButton = document.querySelector('#login-button')
  const onLogin = async event => {
    event.preventDefault()
    const emailValue = document.querySelector('#email').value
    const passwordValue = document.querySelector('#password').value
    if (!emailValue || !passwordValue) {
      Snackbar.show({text: ERROR_MESSAGE.LOGIN_FAIL, pos: 'bottom-center', showAction: false, duration: 2000})
      return
    }

    const loginData = {
      email: emailValue,
      password: passwordValue
    }

    try {
      const tokenResponse = await api.member.login(loginData);
      sessionStorage.setItem('token', tokenResponse.tokenType + ' ' + tokenResponse.accessToken)
      location.href = '/favorites'
    } catch (e) {
      Snackbar.show({text: e, pos: 'bottom-center', showAction: false, duration: 2000})
    }
  }

  this.init = () => {
    $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin)
  }
}

const login = new Login()
login.init()
