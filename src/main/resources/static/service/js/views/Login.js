import {ERROR_MESSAGE, EVENT_TYPE, SUCCESS} from '../../utils/constants.js'
import api from "../../api/index.js";

function Login() {
  const $loginButton = document.querySelector('#login-button')
  const $email = document.querySelector('#email')
  const $password = document.querySelector('#password')

  const onLogin = async event => {
    event.preventDefault()
    if (event.type !== EVENT_TYPE.CLICK && event.key !== "Enter") {
      return;
    }
    const requestData = {
      email: $email.value,
      password: $password.value
    }

    if (!$email.value && !$password.value) {
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
    $password.addEventListener(EVENT_TYPE.KEY_UP, onLogin)
    $email.addEventListener(EVENT_TYPE.KEY_UP, onLogin)
  }
}

const login = new Login()
login.init()
