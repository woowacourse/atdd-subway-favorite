import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import api from '../../api/index.js'
import showSnackbar from "../../lib/snackbar/index.js";

function Login() {
  const $loginButton = document.querySelector('#login-button')
  const onLogin = event => {
    event.preventDefault()
    const emailValue = document.querySelector('#email').value
    const passwordValue = document.querySelector('#password').value
    if (!emailValue && !passwordValue) {
      showSnackbar(ERROR_MESSAGE.LOGIN_FAIL)
      return
    }

    const data = {
      email: emailValue,
      password: passwordValue
    }

    api.member.login(data)
      .then(response => {
        const location = response.headers.get('Location')
        localStorage.setItem("memberId", `${location.split("/")[2]}`)
        return response.json()
      })
      .then(auth => {
        if (!!auth) {
          localStorage.setItem("auth", `${auth.tokenType} ${auth.accessToken}`)
          location.href = '/'
        }

      }).catch(() => showSnackbar(ERROR_MESSAGE.LOGIN_FAIL))
  }

  this.init = () => {
    $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin)
  }
}

const login = new Login()
login.init()
