import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import api from "../../api/index.js";

function Login() {
  const $loginButton = document.querySelector('#login-button')
  const onLogin = async event => {
    event.preventDefault()
    const emailValue = document.querySelector('#email').value
    const passwordValue = document.querySelector('#password').value
    if (!emailValue || !passwordValue) {
      alert(ERROR_MESSAGE.LOGIN_FAIL)
      return
    }

    const loginData = {
      email: emailValue,
      password: passwordValue
    }

    try {
      await api.member.login(loginData);
      location.href = '/favorites'
    } catch (e) {
      alert(e)
    }
  }

  this.init = () => {
    $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin)
  }
}

const login = new Login()
login.init()
