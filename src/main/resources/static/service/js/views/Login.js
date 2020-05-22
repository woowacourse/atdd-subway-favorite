import {ERROR_MESSAGE, EVENT_TYPE, SUCCESS} from '../../utils/constants.js'
import api from "../../api/index.js";

function Login() {
  const $loginButton = document.querySelector('#login-button')
  const onLogin = async event => {
    event.preventDefault()
    const emailValue = document.querySelector('#email').value
    const passwordValue = document.querySelector('#password').value
    const requestData = {
      email: emailValue,
      password: passwordValue
    }

    if (!emailValue && !passwordValue) {
      Snackbar.show({text: ERROR_MESSAGE.LOGIN_FAIL, pos: 'bottom-center', showAction: false, duration: 2000})
      return
    }

    api.member.login(requestData).then(async response => {
      if (!response.ok) {
        return Snackbar.show({
          text: '😭' + await response.text(),
          pos: 'bottom-center',
          showAction: false,
          duration: 2000
        })
      }
      const tokenResponse = await response.json();
      localStorage.setItem("token", tokenResponse.accessToken);
      alert(SUCCESS.LOGIN);
      location.href = "/"
    });

  }

  this.init = () => {
    $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin)
  }
}

const login = new Login()
login.init()
